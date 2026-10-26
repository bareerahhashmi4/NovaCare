package com.example.NovaCare.service;

import com.example.NovaCare.dto.EmergencyAssessmentRequestDTO;
import com.example.NovaCare.model.*;
import com.example.NovaCare.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmergencyServiceImpl implements EmergencyService {

    private final EmergencyCaseRepository repo;
    private final PatientRepository patientRepo;
    private final VitalSignsRepository vitalRepo;
    private final DoctorRepository doctorRepo;
    private final PriorityAssessmentService priorityService;

    // Sabse low priority ka number sabse bada hai (LOW = 4)
    private static final Map<PriorityLevel, Integer> PRIORITY_ORDER = Map.of(
            PriorityLevel.CRITICAL, 1,
            PriorityLevel.HIGH, 2,
            PriorityLevel.MEDIUM, 3,
            PriorityLevel.LOW, 4
    );

    // ================= CREATE =================
    @Override
    @Transactional
    public EmergencyCase createEmergencyCase(EmergencyAssessmentRequestDTO dto) {
        log.info("Creating new emergency case for patient: {}", dto.getPatient().getName());

        Patient patient = patientRepo.save(
                Patient.builder()
                        .name(dto.getPatient().getName())
                        .age(dto.getPatient().getAge())
                        .gender(dto.getPatient().getGender())
                        .bloodGroup(dto.getPatient().getBloodGroup())
                        .location(dto.getPatient().getLocation())
                        .contactNumber(dto.getPatient().getContactNumber())
                        .email(dto.getPatient().getEmail())
                        .build()
        );

        VitalSigns vital = vitalRepo.save(
                VitalSigns.builder()
                        .patientId(patient.getId())
                        .heartRate(dto.getVitalSigns().getHeartRate())
                        .oxygenLevel(dto.getVitalSigns().getOxygenLevel())
                        .bloodPressure(dto.getVitalSigns().getBloodPressure())
                        .symptoms(dto.getVitalSigns().getSymptoms())
                        .temperature(dto.getVitalSigns().getTemperature())
                        .build()
        );

        PriorityLevel priority = priorityService.assessPriority(
                vital.getHeartRate(),
                vital.getOxygenLevel(),
                vital.getBloodPressure(),
                vital.getSymptoms()
        );

        EmergencyCase ec = EmergencyCase.builder()
                .patientId(patient.getId())
                .patientName(patient.getName())
                .vitalSignsId(vital.getId())
                .priority(priority != null ? priority : PriorityLevel.LOW)
                .status(EmergencyStatus.WAITING)
                .symptoms(vital.getSymptoms())
                .assessment(dto.getAssessment())
                .createdAt(LocalDateTime.now())
                .build();

        return repo.save(ec);
    }

    // ================= ASSIGN =================
    @Transactional
    public synchronized EmergencyCase attemptAssignment(EmergencyCase ec) {
        EmergencyCase fresh = repo.findById(ec.getId()).orElse(ec);

        // Agar case already assign ho chuka hai toh skip karein
        if (fresh.getStatus() != EmergencyStatus.WAITING) {
            return fresh;
        }

        // Khali (ONLINE) doctor dhoondein
        Doctor doctor = doctorRepo.findFirstByAvailability(DoctorAvailability.ONLINE)
                .orElse(null);

        // PREEMPTION LOGIC (Sirf CRITICAL cases ke liye)
        if (doctor == null && fresh.getPriority() == PriorityLevel.CRITICAL) {
            log.info("No online doctor available for CRITICAL case {}. Checking for preemption...", fresh.getId());

            List<EmergencyCase> assignedCases = repo.findByStatusOrderByCreatedAtDesc(EmergencyStatus.ASSIGNED);

            // FIX: .max() use kiya taake sabse weak/low priority case (jaise LOW=4 ya MEDIUM=3) pehle pause ho, na ke HIGH.
            Optional<EmergencyCase> lowestPriorityCase = assignedCases.stream()
                    .filter(c -> c.getPriority() != PriorityLevel.CRITICAL)
                    .max(Comparator.comparingInt(c -> PRIORITY_ORDER.getOrDefault(c.getPriority(), 99)));

            if (lowestPriorityCase.isPresent()) {
                EmergencyCase oldCase = lowestPriorityCase.get();

                doctor = doctorRepo.findById(oldCase.getAssignedDoctorId()).orElse(null);

                if (doctor != null) {
                    log.warn("Preempting Doctor {} from {} case {} to CRITICAL case {}",
                            doctor.getName(), oldCase.getPriority(), oldCase.getId(), fresh.getId());

                    // Purane running case ko pause (WAITING) karein
                    oldCase.setAssignedDoctorId(null);
                    oldCase.setAssignedDoctorName(null);
                    oldCase.setStatus(EmergencyStatus.WAITING);
                    oldCase.setAssignedAt(null); // Reset assignment time
                    repo.save(oldCase);
                }
            }
        }

        // Agar abhi bhi koi doctor nahi mila (e.g., saare doctors already CRITICAL par hain)
        if (doctor == null) {
            return fresh;
        }

        // Doctor ko naye case par map karein
        fresh.setAssignedDoctorId(doctor.getId());
        fresh.setAssignedDoctorName(doctor.getName());
        fresh.setStatus(EmergencyStatus.ASSIGNED);
        fresh.setAssignedAt(LocalDateTime.now());

        doctor.setAvailability(DoctorAvailability.BUSY);
        doctor.setAssignedPatientId(fresh.getPatientId());
        doctor.setAssignedPatientName(fresh.getPatientName());

        doctorRepo.save(doctor);
        return repo.save(fresh);
    }

    // ================= SCHEDULER =================
    @Scheduled(fixedDelay = 3000)
    @Transactional // FIX: Taake loop ke andar self-invocation proxy breakdown na kare aur updates database mein save hon
    public void runScheduler() {
        List<EmergencyCase> waitingCases = repo.findByStatusOrderByCreatedAtDesc(EmergencyStatus.WAITING);

        if (waitingCases.isEmpty()) return;

        // Waiting cases ko unki priority ke hisab se sort karein (CRITICAL pehle, LOW baad mein)
        waitingCases.sort(Comparator.comparingInt(c -> PRIORITY_ORDER.getOrDefault(c.getPriority(), 99)));

        for (EmergencyCase ec : waitingCases) {
            // Agar koi doctor online nahi hai aur case CRITICAL nahi hai, toh loop break karein (Preemption chalane ki zaroorat nahi)
            if (ec.getPriority() != PriorityLevel.CRITICAL &&
                    !doctorRepo.existsByAvailability(DoctorAvailability.ONLINE)) {
                break;
            }
            attemptAssignment(ec);
        }
    }

    // ================= AUTO RESOLVE =================
    // ================= AUTO RESOLVE (UPDATED) =================
// ================= AUTO RESOLVE (UPDATED FOR 2 MINS) =================
    @Scheduled(fixedDelay = 10000) // Ab yeh har 10 seconds baad check karega
    @Transactional
    public void autoResolveCases() {
        List<EmergencyCase> assignedCases = repo.findByStatusOrderByCreatedAtDesc(EmergencyStatus.ASSIGNED);

        for (EmergencyCase ec : assignedCases) {
            if (ec.getAssignedAt() == null) continue;

            // CRITICAL ko exact 2 minutes (120s) diya hai, baaqi priorities ko us hisab se barha diya hai
            int timeoutSeconds = switch (ec.getPriority()) {
                case CRITICAL -> 120;  // Exact 2 Minutes
                case HIGH -> 180;      // 3 Minutes
                case MEDIUM -> 240;    // 4 Minutes
                default -> 300;        // 5 Minutes (LOW ke liye)
            };

            // Agar assigned time mein timeoutSeconds plus karne ke baad bhi woh waqt guzar chuka hai
            if (ec.getAssignedAt().plusSeconds(timeoutSeconds).isBefore(LocalDateTime.now())) {
                log.info("Auto-resolving case {} due to timeout.", ec.getId());

                ec.setStatus(EmergencyStatus.RESOLVED);
                ec.setResolvedAt(LocalDateTime.now());

                if (ec.getAssignedDoctorId() != null) {
                    doctorRepo.findById(ec.getAssignedDoctorId()).ifPresent(doc -> {
                        doc.setAvailability(DoctorAvailability.ONLINE);
                        doc.setAssignedPatientId(null);
                        doc.setAssignedPatientName(null);

                        doc.setPatientsHandled(doc.getPatientsHandled() == null ? 1 : doc.getPatientsHandled() + 1);
                        doc.setTotalCasesResolved(doc.getTotalCasesResolved() == null ? 1 : doc.getTotalCasesResolved() + 1);

                        doctorRepo.save(doc);
                    });
                }
                repo.save(ec);
            }
        }
    }
    // ================= UPDATE STATUS =================
    @Override
    @Transactional
    public EmergencyCase updateStatus(String caseId, String status) {
        EmergencyCase ec = getById(caseId);
        EmergencyStatus newStatus = EmergencyStatus.valueOf(status.toUpperCase());

        ec.setStatus(newStatus);

        if (newStatus == EmergencyStatus.RESOLVED) {
            ec.setResolvedAt(LocalDateTime.now());

            if (ec.getAssignedDoctorId() != null) {
                doctorRepo.findById(ec.getAssignedDoctorId()).ifPresent(doc -> {
                    doc.setAvailability(DoctorAvailability.ONLINE);
                    doc.setAssignedPatientId(null);
                    doc.setAssignedPatientName(null);

                    doc.setPatientsHandled(doc.getPatientsHandled() == null ? 1 : doc.getPatientsHandled() + 1);
                    doc.setTotalCasesResolved(doc.getTotalCasesResolved() == null ? 1 : doc.getTotalCasesResolved() + 1);

                    doctorRepo.save(doc);
                });
            }
        }

        return repo.save(ec);
    }

    // ================= MANUAL ASSIGN =================
    @Override
    @Transactional
    public EmergencyCase assignDoctor(String caseId, String doctorId) {
        EmergencyCase ec = getById(caseId);
        Doctor doctor = doctorRepo.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // Agar doctor pehle kisi case par tha, toh use clear karein (Optional Safety Check)
        ec.setAssignedDoctorId(doctor.getId());
        ec.setAssignedDoctorName(doctor.getName());
        ec.setStatus(EmergencyStatus.ASSIGNED);
        ec.setAssignedAt(LocalDateTime.now());

        doctor.setAvailability(DoctorAvailability.BUSY);
        doctor.setAssignedPatientId(ec.getPatientId());
        doctor.setAssignedPatientName(ec.getPatientName());

        doctorRepo.save(doctor);
        return repo.save(ec);
    }

    // ================= READ =================
    @Override
    public List<EmergencyCase> getAllCases() {
        return repo.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public EmergencyCase getById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Case not found: " + id));
    }

    @Override
    public List<EmergencyCase> getByPriority(String priority) {
        return repo.findByPriorityOrderByCreatedAtDesc(
                PriorityLevel.valueOf(priority.toUpperCase())
        );
    }

    @Override
    public List<EmergencyCase> getByStatus(String status) {
        return repo.findByStatusOrderByCreatedAtDesc(
                EmergencyStatus.valueOf(status.toUpperCase())
        );
    }
}
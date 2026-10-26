package com.example.NovaCare.service;

import com.example.NovaCare.model.Doctor;
import com.example.NovaCare.model.DoctorAvailability;
import com.example.NovaCare.dto.DoctorDTO;
import com.example.NovaCare.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorDTO createDoctor(DoctorDTO doctorDTO) {

        if (doctorRepository.findByEmail(doctorDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Doctor with this email already exists");
        }

        Doctor doctor = Doctor.builder()
                .name(doctorDTO.getName())
                .specialization(doctorDTO.getSpecialization())
                .email(doctorDTO.getEmail())
                .contactNumber(doctorDTO.getContactNumber())
                .availability(DoctorAvailability.ONLINE)
                .qualification(doctorDTO.getQualification())
                .experience(doctorDTO.getExperience())
                .patientsHandled(0)
                .totalCasesResolved(0)
                .build();

        return toDTO(doctorRepository.save(doctor));
    }

    public DoctorDTO getDoctorById(String id) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + id));

        return toDTO(doctor);
    }

    public List<DoctorDTO> getAllDoctors() {

        return doctorRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<DoctorDTO> getDoctorsByAvailability(String availability) {

        try {
            DoctorAvailability status = DoctorAvailability.valueOf(availability.toUpperCase());

            return doctorRepository.findByAvailability(status)
                    .stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid availability status: " + availability);
        }
    }

    public List<DoctorDTO> getDoctorsBySpecialization(String specialization) {

        return doctorRepository.findBySpecialization(specialization)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public DoctorDTO updateDoctor(String id, DoctorDTO doctorDTO) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + id));

        doctor.setName(doctorDTO.getName());
        doctor.setSpecialization(doctorDTO.getSpecialization());
        doctor.setContactNumber(doctorDTO.getContactNumber());
        doctor.setQualification(doctorDTO.getQualification());
        doctor.setExperience(doctorDTO.getExperience());

        return toDTO(doctorRepository.save(doctor));
    }

    public DoctorDTO updateDoctorAvailability(String id, String availability) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + id));

        try {
            DoctorAvailability status = DoctorAvailability.valueOf(availability.toUpperCase());
            doctor.setAvailability(status);

            return toDTO(doctorRepository.save(doctor));

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid availability status: " + availability);
        }
    }

    public void deleteDoctor(String id) {

        if (!doctorRepository.existsById(id)) {
            throw new RuntimeException("Doctor not found with ID: " + id);
        }

        doctorRepository.deleteById(id);
    }

    public DoctorDTO releaseDoctor(String doctorId) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        doctor.setAssignedPatientId(null);
        doctor.setAssignedPatientName(null);
        doctor.setAvailability(DoctorAvailability.ONLINE);

        // optional improvement (recommended)
        doctor.setPatientsHandled(
                doctor.getPatientsHandled() == null ? 0 : doctor.getPatientsHandled()
        );

        return toDTO(doctorRepository.save(doctor));
    }

    private DoctorDTO toDTO(Doctor doctor) {

        return new DoctorDTO(
                doctor.getId(),
                doctor.getName(),
                doctor.getSpecialization(),
                doctor.getEmail(),
                doctor.getContactNumber(),
                doctor.getAvailability(),
                doctor.getPatientsHandled() != null ? doctor.getPatientsHandled() : 0,
                doctor.getTotalCasesResolved() != null ? doctor.getTotalCasesResolved() : 0,
                doctor.getQualification(),
                doctor.getExperience()
        );
    }
}
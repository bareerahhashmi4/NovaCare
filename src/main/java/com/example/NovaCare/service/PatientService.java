package com.example.NovaCare.service;

import com.example.NovaCare.model.Patient;
import com.example.NovaCare.dto.PatientDTO;
import com.example.NovaCare.model.PatientStatus;
import com.example.NovaCare.model.PriorityLevel;
import com.example.NovaCare.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientDTO createPatient(PatientDTO patientDTO) {
        // Check if patient with same contact number already exists
        if (patientRepository.findByContactNumber(patientDTO.getContactNumber()).isPresent()) {
            throw new RuntimeException("Patient with this contact number already exists");
        }

        Patient patient = Patient.builder()
                .name(patientDTO.getName())
                .age(patientDTO.getAge())
                .gender(patientDTO.getGender())
                .bloodGroup(patientDTO.getBloodGroup())
                .location(patientDTO.getLocation())
                .contactNumber(patientDTO.getContactNumber())
                .email(patientDTO.getEmail())
                .medicalHistory(patientDTO.getMedicalHistory())
                .priority(patientDTO.getPriority() != null
                        ? patientDTO.getPriority()
                        : PriorityLevel.MEDIUM)
                .status(PatientStatus.WAITING)
                .build();

        Patient saved = patientRepository.save(patient);
        return toDTO(saved);
    }

    public PatientDTO getPatientById(String id) {
        return patientRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + id));
    }

    public PatientDTO getPatientByContactNumber(String contactNumber) {
        return patientRepository.findByContactNumber(contactNumber)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Patient not found with contact number: " + contactNumber));
    }

    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public PatientDTO updatePatient(String id, PatientDTO patientDTO) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + id));

        patient.setName(patientDTO.getName());
        patient.setAge(patientDTO.getAge());
        patient.setGender(patientDTO.getGender());
        patient.setBloodGroup(patientDTO.getBloodGroup());
        patient.setLocation(patientDTO.getLocation());
        patient.setEmail(patientDTO.getEmail());
        patient.setMedicalHistory(patientDTO.getMedicalHistory());

        Patient updated = patientRepository.save(patient);
        return toDTO(updated);
    }

    public void deletePatient(String id) {
        if (!patientRepository.existsById(id)) {
            throw new RuntimeException("Patient not found with ID: " + id);
        }
        patientRepository.deleteById(id);
    }

    private PatientDTO toDTO(Patient patient) {
        return new PatientDTO(
                patient.getId(),
                patient.getName(),
                patient.getAge(),
                patient.getGender(),
                patient.getBloodGroup(),
                patient.getLocation(),
                patient.getContactNumber(),
                patient.getEmail(),
                patient.getMedicalHistory(),
                patient.getPriority()
        );


    }
}

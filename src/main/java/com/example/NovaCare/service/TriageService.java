package com.example.NovaCare.service;

import com.example.NovaCare.model.*;
import com.example.NovaCare.repository.DoctorRepository;
import com.example.NovaCare.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TriageService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Transactional
    public void dispatch() {

        // 1. Get any WAITING patient
        Patient patient = patientRepository.findAll()
                .stream()
                .filter(p -> p.getStatus() == PatientStatus.WAITING)
                .findFirst()
                .orElse(null);

        if (patient == null) return;

        // 2. Get available doctor
        Doctor doctor = doctorRepository.findFirstByAvailability(DoctorAvailability.AVAILABLE)
                .orElse(null);

        if (doctor == null) return;

        assign(doctor, patient);
    }

    private void assign(Doctor doctor, Patient patient) {

        doctor.setAvailability(DoctorAvailability.BUSY);
        doctor.setAssignedPatientId(patient.getId());
        doctor.setAssignedPatientName(patient.getName());

        patient.setStatus(PatientStatus.ASSIGNED);

        doctorRepository.save(doctor);
        patientRepository.save(patient);
    }

    public void releaseDoctorIfNeeded(Doctor doctor) {

        doctor.setAvailability(DoctorAvailability.AVAILABLE);
        doctor.setAssignedPatientId(null);
        doctor.setAssignedPatientName(null);

        doctorRepository.save(doctor);
    }
}
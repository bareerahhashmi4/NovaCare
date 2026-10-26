package com.example.NovaCare.repository;

import com.example.NovaCare.model.Patient;
import com.example.NovaCare.model.PatientStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends MongoRepository<Patient, String> {
    Optional<Patient> findByContactNumber(String contactNumber);
    Optional<Patient> findByEmail(String email);
    Optional<Patient> findTopByStatusOrderByPriorityAsc(PatientStatus status);
}

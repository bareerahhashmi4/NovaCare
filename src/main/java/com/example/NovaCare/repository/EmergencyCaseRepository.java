package com.example.NovaCare.repository;



import com.example.NovaCare.model.EmergencyStatus;
import com.example.NovaCare.model.PriorityLevel;
import com.example.NovaCare.model.EmergencyCase;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface EmergencyCaseRepository extends MongoRepository<EmergencyCase, String> {

    List<EmergencyCase> findAllByOrderByCreatedAtDesc();

    List<EmergencyCase> findByPriorityOrderByCreatedAtDesc(PriorityLevel priority);

    List<EmergencyCase> findByStatusOrderByCreatedAtDesc(EmergencyStatus status);

    Optional<EmergencyCase> findByPatientId(String patientId);
}
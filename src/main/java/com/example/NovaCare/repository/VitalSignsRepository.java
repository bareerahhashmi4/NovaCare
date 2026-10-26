package com.example.NovaCare.repository;

import com.example.NovaCare.model.VitalSigns;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VitalSignsRepository extends MongoRepository<VitalSigns, String> {
    List<VitalSigns> findByPatientIdOrderByRecordedAtDesc(String patientId);
    Optional<VitalSigns> findByPatientId(String patientId);
}

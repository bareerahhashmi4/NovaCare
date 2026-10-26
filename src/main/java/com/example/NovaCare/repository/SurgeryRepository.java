package com.example.NovaCare.repository;

import com.example.NovaCare.model.Surgery;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface SurgeryRepository extends MongoRepository<Surgery, String> {
    List<Surgery> findByStatus(String status);
    List<Surgery> findByOrderByEstimatedDurationAsc();

    // Delete all completed surgeries
    void deleteByStatus(String status);
}
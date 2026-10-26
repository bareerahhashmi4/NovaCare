package com.example.NovaCare.repository;

import com.example.NovaCare.model.LabTest;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface LabTestRepository extends MongoRepository<LabTest, String> {
    List<LabTest> findByStatus(String status);
    List<LabTest> findByStatusOrderByWaitingTimeAsc(String status);
    void deleteByStatus(String status);
}
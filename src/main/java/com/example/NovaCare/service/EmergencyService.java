package com.example.NovaCare.service;



import com.example.NovaCare.dto.EmergencyAssessmentRequestDTO;
import com.example.NovaCare.model.EmergencyCase;

import java.util.List;

public interface EmergencyService {

    EmergencyCase createEmergencyCase(EmergencyAssessmentRequestDTO dto);

    List<EmergencyCase> getAllCases();

    EmergencyCase getById(String id);

    EmergencyCase assignDoctor(String caseId, String doctorId);

    EmergencyCase updateStatus(String caseId, String status);

    List<EmergencyCase> getByPriority(String priority);

    List<EmergencyCase> getByStatus(String status);
}
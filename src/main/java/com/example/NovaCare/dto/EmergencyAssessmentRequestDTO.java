package com.example.NovaCare.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyAssessmentRequestDTO {

    private PatientDTO patient;
    private VitalSignsDTO vitalSigns;
    private String assessment;
}
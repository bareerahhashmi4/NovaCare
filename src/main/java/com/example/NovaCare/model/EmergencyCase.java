package com.example.NovaCare.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "emergency_cases")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyCase {

    @Id
    private String id;
    private String patientName;
    private String patientId;
    private String vitalSignsId;

    private PriorityLevel priority;
    private EmergencyStatus status;

    private String assignedDoctorId;
    private String assignedDoctorName;

    private String symptoms;
    private String assessment;


    private LocalDateTime createdAt;
    private LocalDateTime assignedAt;
    private LocalDateTime resolvedAt;
}
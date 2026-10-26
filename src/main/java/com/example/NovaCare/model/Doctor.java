package com.example.NovaCare.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;



@Document(collection = "doctors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    private String id;

    private String name;
    private String specialization;
    private String email;
    private String contactNumber;

    private DoctorAvailability availability;


    private String qualification;
    private String experience;

    // 🔥 for assignment tracking
    private String assignedPatientId;
    private String assignedPatientName;
    @Builder.Default
    private Integer patientsHandled = 0;

    @Builder.Default
    private Integer totalCasesResolved = 0;
}
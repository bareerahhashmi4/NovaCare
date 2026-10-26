package com.example.NovaCare.dto;

import com.example.NovaCare.model.PriorityLevel;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {

    private String id;      // MongoDB IDs are usually String

    private String name;
    private Integer age;
    private String gender;
    private String bloodGroup;
    private String location;
    private String contactNumber;

    @Email(message = "Email should be valid")
    private String email;

    private String medicalHistory;

    private PriorityLevel priority;
}
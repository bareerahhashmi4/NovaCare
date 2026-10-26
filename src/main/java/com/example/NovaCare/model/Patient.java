package com.example.NovaCare.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Document(collection = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {
    @Id
    private String id;
    private PriorityLevel priority;
    @NotBlank(message = "Patient name is required")
    private String name;

    @Positive(message = "Age must be positive")
    @NotNull(message = "Age is required")
    private Integer age;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotBlank(message = "Blood group is required")
    private String bloodGroup;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^[0-9]{10,}$", message = "Invalid contact number")
    private String contactNumber;

    @Email(message = "Email should be valid")
    private String email;

    private String medicalHistory;
    private PatientStatus status = PatientStatus.WAITING;
    private LocalDateTime createdAt = LocalDateTime.now();
}


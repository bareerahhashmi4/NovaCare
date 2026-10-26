package com.example.NovaCare.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Document(collection = "vital_signs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VitalSigns {
    @Id
    private String id;

    @NotBlank(message = "Patient ID is required")
    private String patientId;

    @NotNull(message = "Heart rate is required")
    @Min(value = 40, message = "Heart rate must be at least 40 BPM")
    @Max(value = 180, message = "Heart rate must not exceed 180 BPM")
    private Integer heartRate;

    @NotNull(message = "Oxygen level is required")
    @Min(value = 50, message = "Oxygen level must be at least 50%")
    @Max(value = 100, message = "Oxygen level must not exceed 100%")
    private Integer oxygenLevel;

    @NotNull(message = "Blood pressure is required")
    @Min(value = 60, message = "Blood pressure must be at least 60")
    @Max(value = 180, message = "Blood pressure must not exceed 180")
    private Integer bloodPressure;

    private String symptoms;
    private Double temperature;

    private LocalDateTime recordedAt = LocalDateTime.now();
}

package com.example.NovaCare.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VitalSignsDTO {

    private String id;
    private String patientId;
    private Integer heartRate;
    private Integer oxygenLevel;
    private Integer bloodPressure;
    private Double temperature;
    private String symptoms;
}

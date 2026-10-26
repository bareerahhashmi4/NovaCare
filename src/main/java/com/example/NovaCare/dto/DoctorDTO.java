package com.example.NovaCare.dto;

import com.example.NovaCare.model.DoctorAvailability;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "doctors")
public class DoctorDTO {

    private String id;
    private String name;
    private String specialization;
    private String email;
    private String contactNumber;

    private DoctorAvailability availability;

    private Integer patientsHandled;
    private Integer totalCasesResolved;

    private String qualification;
    private String experience;
}
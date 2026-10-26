package com.example.NovaCare.controller;


import com.example.NovaCare.dto.PatientDTO;
import com.example.NovaCare.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<PatientDTO> createPatient(@RequestBody PatientDTO patientDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(patientService.createPatient(patientDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatient(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(String.valueOf(id)));
    }

    @GetMapping
    public ResponseEntity<?> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

}



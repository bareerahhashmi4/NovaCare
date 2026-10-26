package com.example.NovaCare.controller;

import com.example.NovaCare.dto.DoctorDTO;
import com.example.NovaCare.service.DoctorService;
import com.example.NovaCare.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DoctorController {

    private final DoctorService doctorService;
    private final DoctorRepository doctorRepository;


    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable String id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @PostMapping
    public ResponseEntity<DoctorDTO> createDoctor(@RequestBody DoctorDTO doctorDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(doctorService.createDoctor(doctorDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorDTO> updateDoctor(
            @PathVariable String id,
            @RequestBody DoctorDTO doctorDTO) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, doctorDTO));
    }

    @PutMapping("/{id}/availability")
    public ResponseEntity<DoctorDTO> updateAvailability(
            @PathVariable String id,
            @RequestParam String availability) {
        return ResponseEntity.ok(doctorService.updateDoctorAvailability(id, availability));
    }

    @GetMapping("/availability/{availability}")
    public ResponseEntity<List<DoctorDTO>> getDoctorsByAvailability(
            @PathVariable String availability) {
        return ResponseEntity.ok(doctorService.getDoctorsByAvailability(availability));
    }

    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<DoctorDTO>> getDoctorsBySpecialization(
            @PathVariable String specialization) {
        return ResponseEntity.ok(doctorService.getDoctorsBySpecialization(specialization));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable String id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }




    @PutMapping("/{doctorId}/release")
    public DoctorDTO releaseDoctor(@PathVariable String doctorId) {
        return doctorService.releaseDoctor(doctorId);
    }

    }

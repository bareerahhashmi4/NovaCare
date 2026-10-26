package com.example.NovaCare.controller;


import com.example.NovaCare.dto.*;
import com.example.NovaCare.model.EmergencyCase;
import com.example.NovaCare.service.EmergencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emergency")
@RequiredArgsConstructor
@CrossOrigin
public class EmergencyController {

    private final EmergencyService service;

    @PostMapping("/assess")
    public EmergencyCase create(@RequestBody EmergencyAssessmentRequestDTO dto) {
        return service.createEmergencyCase(dto);
    }

    @GetMapping("/cases")
    public List<EmergencyCase> getAll() {
        return service.getAllCases();
    }

    @GetMapping("/cases/{id}")
    public EmergencyCase getById(@PathVariable String id) {
        return service.getById(id);
    }

    @PutMapping("/cases/{id}/assign/{doctorId}")
    public EmergencyCase assign(@PathVariable String id, @PathVariable String doctorId) {
        return service.assignDoctor(id, doctorId);
    }

    @PutMapping("/cases/{id}/status/{status}")
    public EmergencyCase updateStatus(@PathVariable String id, @PathVariable String status) {
        return service.updateStatus(id, status);
    }

    @GetMapping("/cases/priority/{priority}")
    public List<EmergencyCase> byPriority(@PathVariable String priority) {
        return service.getByPriority(priority);
    }

    @GetMapping("/cases/status/{status}")
    public List<EmergencyCase> byStatus(@PathVariable String status) {
        return service.getByStatus(status);
    }
}
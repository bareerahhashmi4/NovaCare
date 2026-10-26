package com.example.NovaCare.controller;

import com.example.NovaCare.model.LabTest;
import com.example.NovaCare.service.RoundRobinLabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/labtest")
@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500"})
public class LabTestController {

    @Autowired
    private RoundRobinLabService labService;

    @PostMapping("/add")
    public ResponseEntity<?> addLabTest(@RequestBody LabTest test) {
        LabTest saved = labService.addLabTest(test);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/all")
    public ResponseEntity<List<LabTest>> getAllTests() {
        return ResponseEntity.ok(labService.getAllTests());
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentTest() {
        LabTest current = labService.getCurrentTest();
        if (current == null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "No Test Running");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(current);
    }

    @GetMapping("/waiting")
    public ResponseEntity<List<LabTest>> getWaitingQueue() {
        return ResponseEntity.ok(labService.getWaitingQueue());
    }

    @GetMapping("/completed")
    public ResponseEntity<List<LabTest>> getCompletedTests() {
        return ResponseEntity.ok(labService.getCompletedTests());
    }

    @GetMapping("/queue-order")
    public ResponseEntity<List<LabTest>> getRoundRobinOrder() {
        return ResponseEntity.ok(labService.getRoundRobinOrder());
    }

    @PostMapping("/execute-quantum")
    public ResponseEntity<?> executeQuantum() {
        LabTest completed = labService.executeQuantum();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Time quantum executed (5 minutes)");
        if (completed != null) {
            response.put("completed", completed);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/run-all")
    public ResponseEntity<?> runAllTests() {
        labService.executeAllQuantums();
        Map<String, String> response = new HashMap<>();
        response.put("message", "All tests completed via Round Robin");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/clear-completed")
    public ResponseEntity<?> clearCompletedTests() {
        labService.clearCompletedTests();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Completed tests cleared");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/clear-all")
    public ResponseEntity<?> clearAllTests() {
        labService.clearAllTests();
        Map<String, String> response = new HashMap<>();
        response.put("message", "All lab tests cleared");
        return ResponseEntity.ok(response);
    }
}
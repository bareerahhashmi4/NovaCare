package com.example.NovaCare.controller;

import com.example.NovaCare.model.Surgery;
import com.example.NovaCare.service.SJFSurgeryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/surgery")
@CrossOrigin(origins = "http://localhost:5500")
public class SurgeryController {

    @Autowired
    private SJFSurgeryService surgeryService;

    // 1. Add new surgery
    @PostMapping("/add")
    public ResponseEntity<?> addSurgery(@RequestBody Surgery surgery) {
        Surgery saved = surgeryService.addSurgery(surgery);
        return ResponseEntity.ok(saved);
    }

    // 2. Get all surgeries
    @GetMapping("/all")
    public ResponseEntity<List<Surgery>> getAllSurgeries() {
        return ResponseEntity.ok(surgeryService.getAllSurgeries());
    }

    // 3. Get current running surgery
    @GetMapping("/current")
    public ResponseEntity<?> getCurrentSurgery() {
        Surgery current = surgeryService.getCurrentRunningSurgery();
        if (current == null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "No Surgery Assigned");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(current);
    }

    // 4. Get timeline
    @GetMapping("/timeline")
    public ResponseEntity<List<Surgery>> getTimeline() {
        return ResponseEntity.ok(surgeryService.getTimeline());
    }

    // 5. Complete current surgery
    @PostMapping("/complete")
    public ResponseEntity<?> completeSurgery() {
        surgeryService.completeCurrentSurgery();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Surgery completed, next surgery started via SJF");
        return ResponseEntity.ok(response);
    }

    // 6. Apply SJF manually
    @PostMapping("/reschedule")
    public ResponseEntity<?> reschedule() {
        surgeryService.applySJFScheduling();
        Map<String, String> response = new HashMap<>();
        response.put("message", "SJF rescheduling applied");
        return ResponseEntity.ok(response);
    }

    // ✅ NEW 7: Clear all surgeries (complete reset)
    @DeleteMapping("/clear-all")
    public ResponseEntity<?> clearAllSurgeries() {
        surgeryService.clearAllSurgeries();
        Map<String, String> response = new HashMap<>();
        response.put("message", "All surgeries cleared successfully");
        return ResponseEntity.ok(response);
    }

    // ✅ NEW 8: Clear only completed surgeries (timeline only)
    @DeleteMapping("/clear-timeline")
    public ResponseEntity<?> clearTimeline() {
        surgeryService.clearTimeline();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Timeline cleared successfully");
        return ResponseEntity.ok(response);
    }
}
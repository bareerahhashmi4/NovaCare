package com.example.NovaCare.service;

import com.example.NovaCare.model.Surgery;
import com.example.NovaCare.repository.SurgeryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class SJFSurgeryService {

    @Autowired
    private SurgeryRepository surgeryRepository;

    private Surgery currentRunningSurgery = null;

    // Add new surgery and apply SJF
    public Surgery addSurgery(Surgery surgery) {
        surgery.setStatus("WAITING");
        Surgery saved = surgeryRepository.save(surgery);
        applySJFScheduling();
        return saved;
    }

    // Apply Shortest Job First
    public void applySJFScheduling() {
        List<Surgery> waitingSurgeries = surgeryRepository.findByStatus("WAITING");

        // Sort by estimated duration (SJF)
        waitingSurgeries.sort(Comparator.comparingInt(Surgery::getEstimatedDuration));

        // Update waiting time for each
        int cumulativeTime = 0;
        for (Surgery surgery : waitingSurgeries) {
            surgery.setWaitingTime(cumulativeTime);
            cumulativeTime += surgery.getEstimatedDuration();
            surgeryRepository.save(surgery);
        }

        // If no surgery running, start the shortest one
        if (currentRunningSurgery == null && !waitingSurgeries.isEmpty()) {
            startNextSurgery();
        }
    }

    // Start next surgery (shortest waiting)
    public void startNextSurgery() {
        List<Surgery> waitingSurgeries = surgeryRepository.findByStatus("WAITING");
        if (waitingSurgeries.isEmpty()) {
            currentRunningSurgery = null;
            return;
        }

        // Get shortest job
        waitingSurgeries.sort(Comparator.comparingInt(Surgery::getEstimatedDuration));
        Surgery next = waitingSurgeries.get(0);

        next.setStatus("RUNNING");
        next.setStartTime(LocalDateTime.now());
        surgeryRepository.save(next);
        currentRunningSurgery = next;
    }

    // Complete current surgery
    public void completeCurrentSurgery() {
        if (currentRunningSurgery != null) {
            currentRunningSurgery.setStatus("COMPLETED");
            currentRunningSurgery.setEndTime(LocalDateTime.now());
            surgeryRepository.save(currentRunningSurgery);
            currentRunningSurgery = null;

            // Start next surgery
            startNextSurgery();
        }
    }

    // Get all surgeries (for Surgery List)
    public List<Surgery> getAllSurgeries() {
        return surgeryRepository.findAll();
    }

    // Get currently running surgery
    public Surgery getCurrentRunningSurgery() {
        if (currentRunningSurgery != null) {
            return currentRunningSurgery;
        }
        // Check database if any RUNNING exists
        List<Surgery> running = surgeryRepository.findByStatus("RUNNING");
        if (!running.isEmpty()) {
            currentRunningSurgery = running.get(0);
            return currentRunningSurgery;
        }
        return null;
    }

    // Get timeline (completed surgeries)
    public List<Surgery> getTimeline() {
        return surgeryRepository.findByStatus("COMPLETED");
    }

    // ✅ NEW: Clear all surgeries (for reset)
    public void clearAllSurgeries() {
        surgeryRepository.deleteAll();
        currentRunningSurgery = null;
    }

    // ✅ NEW: Clear only completed surgeries (timeline)
    public void clearTimeline() {
        surgeryRepository.deleteByStatus("COMPLETED");
        // Current running surgery remains unchanged
    }
}
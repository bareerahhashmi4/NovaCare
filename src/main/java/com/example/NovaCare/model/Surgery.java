package com.example.NovaCare.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "surgeries")
public class Surgery {
    @Id
    private String id;
    private String patientName;
    private String surgeryType;
    private int estimatedDuration; // in minutes
    private String status; // "WAITING", "RUNNING", "COMPLETED"
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int waitingTime; // calculated

    // Constructors
    public Surgery() {}

    public Surgery(String patientName, String surgeryType, int estimatedDuration) {
        this.patientName = patientName;
        this.surgeryType = surgeryType;
        this.estimatedDuration = estimatedDuration;
        this.status = "WAITING";
    }

    // Getters and Setters (Important for MongoDB)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getSurgeryType() { return surgeryType; }
    public void setSurgeryType(String surgeryType) { this.surgeryType = surgeryType; }

    public int getEstimatedDuration() { return estimatedDuration; }
    public void setEstimatedDuration(int estimatedDuration) { this.estimatedDuration = estimatedDuration; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public int getWaitingTime() { return waitingTime; }
    public void setWaitingTime(int waitingTime) { this.waitingTime = waitingTime; }
}
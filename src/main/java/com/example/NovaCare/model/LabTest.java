package com.example.NovaCare.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "lab_tests")
public class LabTest {
    @Id
    private String id;
    private String patientName;
    private String testType;
    private int estimatedDuration;
    private int remainingTime;
    private String status;  // WAITING, RUNNING, COMPLETED
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int waitingTime;
    private int completedQuantums;

    public LabTest() {}

    public LabTest(String patientName, String testType, int estimatedDuration) {
        this.patientName = patientName;
        this.testType = testType;
        this.estimatedDuration = estimatedDuration;
        this.remainingTime = estimatedDuration;
        this.status = "WAITING";
        this.completedQuantums = 0;
        this.waitingTime = 0;
    }

    // Getters
    public String getId() { return id; }
    public String getPatientName() { return patientName; }
    public String getTestType() { return testType; }
    public int getEstimatedDuration() { return estimatedDuration; }
    public int getRemainingTime() { return remainingTime; }
    public String getStatus() { return status; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public int getWaitingTime() { return waitingTime; }
    public int getCompletedQuantums() { return completedQuantums; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public void setTestType(String testType) { this.testType = testType; }
    public void setEstimatedDuration(int estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
        this.remainingTime = estimatedDuration;
    }
    public void setRemainingTime(int remainingTime) { this.remainingTime = remainingTime; }
    public void setStatus(String status) { this.status = status; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public void setWaitingTime(int waitingTime) { this.waitingTime = waitingTime; }
    public void setCompletedQuantums(int completedQuantums) { this.completedQuantums = completedQuantums; }
}
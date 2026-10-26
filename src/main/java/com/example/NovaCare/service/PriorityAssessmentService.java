package com.example.NovaCare.service;

import com.example.NovaCare.model.PriorityLevel;
import org.springframework.stereotype.Service;

@Service
public class PriorityAssessmentService {

    public PriorityLevel assessPriority(Integer heartRate, Integer oxygenLevel,
                                        Integer bloodPressure, String symptoms) {

        // Critical conditions
        if (heartRate > 140 || heartRate < 40 || oxygenLevel < 70 || bloodPressure > 160) {
            return PriorityLevel.CRITICAL;
        }

        // High priority
        if (heartRate > 120 || oxygenLevel < 80 || bloodPressure > 140) {
            return PriorityLevel.HIGH;
        }

        // Medium priority
        if (heartRate > 100 || oxygenLevel < 90) {
            return PriorityLevel.MEDIUM;
        }

        // Check symptoms
        if (symptoms != null && (symptoms.contains("Chest Pain") ||
                symptoms.contains("Stroke Signs") ||
                symptoms.contains("Accident"))) {
            return PriorityLevel.HIGH;
        }

        return PriorityLevel.LOW;
    }
}



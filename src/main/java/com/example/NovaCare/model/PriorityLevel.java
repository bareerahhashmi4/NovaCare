package com.example.NovaCare.model;

public enum PriorityLevel {
    CRITICAL(1),
    HIGH(2),
    MEDIUM(3),
    LOW(4);

    private final int weight;

    PriorityLevel(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }
}
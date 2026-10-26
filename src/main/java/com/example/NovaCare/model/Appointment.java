package com.example.NovaCare.model;


public class Appointment {

    private String patientName;
    private String doctor;
    private String slot;

    public Appointment(String patientName,
                       String doctor,
                       String slot) {

        this.patientName = patientName;
        this.doctor = doctor;
        this.slot = slot;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getDoctor() {
        return doctor;
    }

    public String getSlot() {
        return slot;
    }
}

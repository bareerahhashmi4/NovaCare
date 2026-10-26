package com.example.NovaCare.schedulers;

import com.example.NovaCare.model.Appointment;

import java.util.*;

public class FCFSScheduler {

    private Map<String, Queue<String>> doctorSlots =
            new HashMap<>();

    public FCFSScheduler() {

        doctorSlots.put(
                "drAli",
                new LinkedList<>(
                        Arrays.asList(
                                "10:00 AM",
                                "11:00 AM",
                                "1:00 PM"
                        )
                )
        );

        doctorSlots.put(
                "drSara",
                new LinkedList<>(
                        Arrays.asList(
                                "2:00 PM",
                                "4:00 PM",
                                "6:00 PM"
                        )
                )
        );

        doctorSlots.put(
                "drAhmed",
                new LinkedList<>(
                        Arrays.asList(
                                "9:00 AM",
                                "12:00 PM",
                                "3:00 PM"
                        )
                )
        );
    }

    public Appointment bookAppointment(
            String patientName,
            String doctor) {

        Queue<String> slots =
                doctorSlots.get(doctor);

        if (slots == null || slots.isEmpty()) {
            return null;
        }

        String assignedSlot = slots.poll();

        return new Appointment(
                patientName,
                doctor,
                assignedSlot
        );
    }
}

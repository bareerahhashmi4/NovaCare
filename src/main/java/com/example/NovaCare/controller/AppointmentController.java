package com.example.NovaCare.controller;

import com.example.NovaCare.model.Appointment;
import com.example.NovaCare.schedulers.FCFSScheduler;
import org.springframework.web.bind.annotation.*;


//@CrossOrigin(origins = "http://127.0.0.1:5500")
@CrossOrigin(origins = "*")
@RestController
public class AppointmentController {

    public AppointmentController() {
        System.out.println("APPOINTMENT CONTROLLER CREATED");
    }
    private FCFSScheduler scheduler =
            new FCFSScheduler();

    @GetMapping("/test")
    public String test() {
        return "Working";
    }

    @GetMapping("/book")
    public String testBook() {
        return "BOOK ENDPOINT FOUND";
    }

    @PostMapping("/book")
    public Appointment bookAppointment(
            @RequestParam String patientName,
            @RequestParam String doctor) {

        System.out.println("BOOK API CALLED");

        return scheduler.bookAppointment(
                patientName,
                doctor);
    }
    @GetMapping("/all")
    public String all() {
        return "Controller Loaded";
    }
}




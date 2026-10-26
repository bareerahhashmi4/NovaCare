package com.example.NovaCare.repository;

import com.example.NovaCare.model.Doctor;
import com.example.NovaCare.model.DoctorAvailability;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends MongoRepository<Doctor, String> {

    Optional<Doctor> findByEmail(String email);

    List<Doctor> findByAvailability(DoctorAvailability availability);

    List<Doctor> findBySpecialization(String specialization);

    // FIX 1: replaces findAll().stream().filter(...).findFirst()
    // MongoDB only fetches ONE document instead of the entire collection
    Optional<Doctor> findFirstByAvailability(DoctorAvailability availability);

    // FIX 2: used in the scheduler to short-circuit the loop early
    // MongoDB COUNT query — no document data transferred
    boolean existsByAvailability(DoctorAvailability availability);
}
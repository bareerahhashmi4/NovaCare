package com.example.NovaCare.controller;

import com.example.NovaCare.model.User;
import com.example.NovaCare.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder();

    // REGISTER API
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody User user) {
        System.out.println("Register API called");
        System.out.println(user.getEmail());

        Optional<User> existing =
                userRepository.findByEmail(
                        user.getEmail()
                );

        // Check if email already exists
        if (existing.isPresent()) {

            return ResponseEntity
                    .badRequest()
                    .body("Email already exists");
        }

        // Encrypt password
        user.setPassword(
                encoder.encode(user.getPassword())
        );

        // Save user
        userRepository.save(user);

        return ResponseEntity
                .ok("User Registered Successfully");
    }
    @GetMapping("/all")
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    // LOGIN API
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody User loginData) {

        Optional<User> userOptional =
                userRepository.findByEmail(
                        loginData.getEmail()
                );

        // User not found
        if (userOptional.isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body("User not found");
        }

        User user = userOptional.get();

        // Check password
        if (!encoder.matches(
                loginData.getPassword(),
                user.getPassword()
        )){

            return ResponseEntity
                    .badRequest()
                    .body("Invalid Password");
        }

        // Success response
        Map<String, String> response =
                new HashMap<>();

        response.put("message",
                "Login Successful");

        response.put("username",
                user.getName());

        response.put("email",
                user.getEmail());

        System.out.println("EMAIL = " + loginData.getEmail());
        System.out.println("PASSWORD = " + loginData.getPassword());

        return ResponseEntity.ok(response);
    }
    @Autowired
    MongoTemplate mongoTemplate;

    @GetMapping("/db-name")
    public String dbName() {

        return mongoTemplate
                .getDb()
                .getName();
    }
    @GetMapping("/hello")
    public String hello() {
        return "working";
    }
}

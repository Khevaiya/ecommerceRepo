package com.example.e_commerce.controller;


import com.example.e_commerce.entity.User;
import com.example.e_commerce.repository.UserRepository;
import com.example.e_commerce.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Sign Up
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody User user) {
        // Validate email format
        if (!isValidEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Invalid email format.");
        }

        // Check if email already exists
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(409).body("Email is already registered.");
        }

        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save the new user
        User savedUser = userService.signUp(user);

        // Return success response with user ID in a structured format
        return ResponseEntity.status(201).body(new SuccessResponse("User registered successfully", savedUser.getId()));
    }

    // Sign In
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestParam String email, @RequestParam String password) {
        // Validate email format
        System.out.println("signIn called"+email+" "+password);
        if (!isValidEmail(email)) {
            return ResponseEntity.badRequest().body("Invalid email format.");
        }

        // Find user by email
        Optional<User> userOptional = userService.signIn(email, password);
        // If user exists and password matches
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Generate JWT token (session token)
            String token = generateJwtToken(user);

            // Return success with token
            return ResponseEntity.ok(new SuccessResponse("Login successful", token));
        } else {
            // Invalid credentials
            return ResponseEntity.status(401).body("Invalid credentials.");
        }
    }

    // Helper method to validate email format
    private boolean isValidEmail(String email) {
        // Simple regex to check if the email format is valid
        return email != null && email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }

    // Helper method to generate JWT token
    private String generateJwtToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId()) // User ID as the subject
                .setIssuedAt(new Date())  // Token issue date
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // Token expiration (1 day)
                .signWith(SignatureAlgorithm.HS256, System.getenv("JWT_SECRET_KEY")) // Use environment variable for the secret key
                .compact(); // Compact the JWT and return it as a string
    }

    // Success response wrapper
    @Setter
    @Getter
    private static class SuccessResponse {
        // Getters and setters
        private String message;
        private Object data;

        public SuccessResponse(String message, Object data) {
            this.message = message;
            this.data = data;
        }

    }
}

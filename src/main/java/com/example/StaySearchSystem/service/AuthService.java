package com.example.StaySearchSystem.service;

import com.example.StaySearchSystem.dto.AuthResponse;
import com.example.StaySearchSystem.dto.LoginRequest;
import com.example.StaySearchSystem.dto.RegisterRequest;
import com.example.StaySearchSystem.entity.Role;
import com.example.StaySearchSystem.entity.User;
import com.example.StaySearchSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    
    // BCrypt Password Encoder (Production Ready)
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Simple token generation (you can upgrade to JWT later)
    private String generateToken(Long userId, String email, String role) {
        return userId + ":" + email + ":" + role + ":" + System.currentTimeMillis();
    }

    public AuthResponse register(RegisterRequest request) {
        // Validate input
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email is required");
        }

        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new RuntimeException("Password must be at least 6 characters");
        }

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new RuntimeException("Name is required");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Create new user
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail().toLowerCase().trim()); // Store email in lowercase
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());

        // Encode password with BCrypt (PostgreSQL compatible)
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Set role
        if (request.getRole() != null && request.getRole().equalsIgnoreCase("ADMIN")) {
            user.setRole(Role.ADMIN);
        } else {
            user.setRole(Role.USER);
        }

        // Save user
        User savedUser = userRepository.save(user);

        // Generate token
        String token = generateToken(savedUser.getId(), savedUser.getEmail(), savedUser.getRole().toString());

        return new AuthResponse(
                token,
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole().toString()
        );
    }

    public AuthResponse login(LoginRequest request) {
        // Validate input
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email is required");
        }

        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Password is required");
        }

        // Find user by email (case-insensitive)
        User user = userRepository.findByEmail(request.getEmail().toLowerCase().trim())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // Check password with BCrypt
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // Generate token
        String token = generateToken(user.getId(), user.getEmail(), user.getRole().toString());

        return new AuthResponse(
                token,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().toString()
        );
    }
}
package com.example.StaySearchSystem.controller;

import org.springframework.web.bind.annotation.*;
import com.example.StaySearchSystem.repository.UserRepository;
import com.example.StaySearchSystem.entity.User;
import com.example.StaySearchSystem.entity.Role;

@RestController
@RequestMapping("/api/staysearch/users")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class UserController {

    private final UserRepository userRepo;

    public UserController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // Get User Profile - Anyone can access their own profile
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userRepo.findById(id).orElseThrow();
    }

    // Get current user by email (for frontend)
    @GetMapping("/email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Update user profile (User can update their own)
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(userDetails.getName());
        user.setPhone(userDetails.getPhone());
        user.setAddress(userDetails.getAddress());
        // Don't update email and role here

        return userRepo.save(user);
    }
}
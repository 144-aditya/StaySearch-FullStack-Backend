package com.example.StaySearchSystem.controller;

import org.springframework.web.bind.annotation.*;
import com.example.StaySearchSystem.repository.UserRepository;
import com.example.StaySearchSystem.entity.User;

@RestController
@RequestMapping("/api/staysearch/users")
@CrossOrigin
public class UserController {

    private final UserRepository userRepo;

    public UserController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // Get User Profile
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userRepo.findById(id).orElseThrow();
    }
}

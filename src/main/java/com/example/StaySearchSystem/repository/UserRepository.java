package com.example.StaySearchSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.StaySearchSystem.entity.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    //  Find user by email (for login)
    Optional<User> findByEmail(String email);

    // Check if email already exists (for registration)
    boolean existsByEmail(String email);
}
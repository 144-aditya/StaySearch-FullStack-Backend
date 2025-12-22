package com.example.StaySearchSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.StaySearchSystem.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}

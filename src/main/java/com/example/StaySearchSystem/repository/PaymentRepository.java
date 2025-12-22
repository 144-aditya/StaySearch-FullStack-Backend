package com.example.StaySearchSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.StaySearchSystem.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}

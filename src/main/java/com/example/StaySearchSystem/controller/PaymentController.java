package com.example.StaySearchSystem.controller;

import com.example.StaySearchSystem.dto.BookingPaymentDTO;
import com.example.StaySearchSystem.entity.Booking;
import com.example.StaySearchSystem.entity.User;
import com.example.StaySearchSystem.repository.BookingRepository;
import com.example.StaySearchSystem.repository.PaymentRepository;
import com.example.StaySearchSystem.repository.UserRepository;
import com.example.StaySearchSystem.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/staysearch")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class PaymentController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @PostMapping("/payments")
    public ResponseEntity<Map<String, Object>> createPayment(@RequestBody BookingPaymentDTO dto) {

        User user;

        // Check if userId is provided (logged-in user)
        if (dto.getUserId() != null) {
            // Use existing user
            user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));

            // Update user info if needed
            user.setName(dto.getName());
            user.setEmail(dto.getEmail());
            user.setPhone(dto.getPhone());
            user.setAddress(dto.getAddress());
            user = userRepository.save(user);
        } else {
            // Create new user (for non-logged-in users)
            user = new User();
            user.setName(dto.getName());
            user.setEmail(dto.getEmail());
            user.setPhone(dto.getPhone());
            user.setAddress(dto.getAddress());
            user.setPassword("default123");  // ✅ SIRF YEH EK LINE ADD KI HAI
            user = userRepository.save(user);
        }

        // Booking
        Booking booking = new Booking();
        booking.setPropertyType(dto.getPropertyType());
        booking.setRoomType(dto.getRoomType());
        booking.setBranch(dto.getBranch());
        booking.setCheckIn(LocalDate.parse(dto.getCheckIn()));
        booking.setDuration(dto.getDuration());
        booking.setAmount(dto.getAmount());
        booking.setPaymentMethod(dto.getPaymentMethod());
        booking.setStatus("CONFIRMED");
        booking.setUser(user);
        booking = bookingRepository.save(booking);

        // Payment
        Payment payment = new Payment();
        payment.setTransactionId(dto.getTransactionId());
        payment.setPaymentStatus(dto.getPaymentStatus());
        payment.setBooking(booking);
        paymentRepository.save(payment);

        // Response with userId + bookingId
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Booking & Payment Successful");
        response.put("userId", user.getId());
        response.put("bookingId", booking.getId());

        return ResponseEntity.ok(response);
    }
}
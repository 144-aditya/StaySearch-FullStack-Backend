package com.example.StaySearchSystem.controller;

import com.example.StaySearchSystem.entity.Booking;
import com.example.StaySearchSystem.entity.User;
import com.example.StaySearchSystem.entity.Role;
import com.example.StaySearchSystem.repository.BookingRepository;
import com.example.StaySearchSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    // Get all users (Admin only)
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID (Admin only)
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update user role (Admin only)
    @PutMapping("/users/{id}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String newRole = request.get("role");

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(Role.valueOf(newRole));
        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User role updated successfully");
        response.put("userId", user.getId());
        response.put("newRole", user.getRole());

        return ResponseEntity.ok(response);
    }

    // Get all bookings (Admin only)
    @GetMapping("/bookings")
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // Get booking statistics (Admin only)
    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalBookings", bookingRepository.count());
        stats.put("users", userRepository.findAll());

        return ResponseEntity.ok(stats);
    }

    // Delete user (Admin only)
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User deleted successfully");
        return ResponseEntity.ok(response);
    }

    // ADD THIS METHOD - Fix all bookings data
    @GetMapping("/fix-bookings")
    public ResponseEntity<?> fixBookings() {
        try {
            // Step 1: Get or create default user
            User defaultUser = userRepository.findAll().stream().findFirst().orElse(null);

            if (defaultUser == null) {
                // Create default user if none exists
                defaultUser = new User();
                defaultUser.setName("Default User");
                defaultUser.setEmail("default@staysearch.com");
                defaultUser.setPhone("9999999999");
                defaultUser.setAddress("Default Address");
                defaultUser.setPassword("default123");
                defaultUser.setRole(Role.USER);
                defaultUser = userRepository.save(defaultUser);
            }

            // Step 2: Get all bookings
            List<Booking> bookings = bookingRepository.findAll();
            int fixedCount = 0;

            for (Booking booking : bookings) {
                boolean updated = false;

                // Fix user_id
                if (booking.getUser() == null) {
                    booking.setUser(defaultUser);
                    updated = true;
                }

                // Fix booking_code
                if (booking.getBookingCode() == null || booking.getBookingCode().isEmpty()) {
                    booking.setBookingCode("BK-" + booking.getId());
                    updated = true;
                }

                // Fix amount
                if (booking.getAmount() == null || booking.getAmount() == 0) {
                    double amount = 5000;
                    if ("PG".equals(booking.getPropertyType())) amount = 8000;
                    else if ("Hostel".equals(booking.getPropertyType())) amount = 4000;
                    else if ("Lodge".equals(booking.getPropertyType())) amount = 3000;
                    else if ("Hotel".equals(booking.getPropertyType())) amount = 5000;
                    booking.setAmount(amount);
                    updated = true;
                }

                // Fix status
                if (booking.getStatus() == null || booking.getStatus().isEmpty()) {
                    booking.setStatus("CONFIRMED");
                    updated = true;
                }

                // Fix payment_method
                if (booking.getPaymentMethod() == null || booking.getPaymentMethod().isEmpty()) {
                    booking.setPaymentMethod("cash");
                    updated = true;
                }

                // Fix duration
                if (booking.getDuration() == null || booking.getDuration().isEmpty()) {
                    booking.setDuration("1");
                    updated = true;
                }

                if (updated) {
                    bookingRepository.save(booking);
                    fixedCount++;
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Bookings fixed successfully!");
            response.put("fixedCount", fixedCount);
            response.put("totalBookings", bookings.size());
            response.put("defaultUserId", defaultUser.getId());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
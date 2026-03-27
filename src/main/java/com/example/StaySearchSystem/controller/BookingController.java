package com.example.StaySearchSystem.controller;

import org.springframework.web.bind.annotation.*;
import com.example.StaySearchSystem.entity.Booking;
import com.example.StaySearchSystem.service.BookingService;
import java.util.List;

@RestController
@RequestMapping("/api/staysearch")
@CrossOrigin
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Create Booking (with optional userId in request body)
    @PostMapping("/bookings")
    public Booking createBooking(@RequestBody Booking booking) {
        // If booking has user, use it; otherwise create without user
        return bookingService.createBooking(booking);
    }

    // My Bookings - No change
    @GetMapping("/bookings/user/{userId}")
    public List<Booking> getUserBookings(@PathVariable Long userId) {
        return bookingService.getUserBookings(userId);
    }

    // Cancel Booking - No change
    @PutMapping("/bookings/{id}/cancel")
    public void cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
    }
}
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

    // Create Booking
    @PostMapping("/bookings")
    public Booking createBooking(@RequestBody Booking booking) {
        return bookingService.createBooking(booking);
    }

    // My Bookings
    @GetMapping("/bookings/user/{userId}")
    public List<Booking> getUserBookings(@PathVariable Long userId) {
        return bookingService.getUserBookings(userId);
    }

    //Cancel Booking
    @PutMapping("/bookings/{id}/cancel")
    public void cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
    }
}

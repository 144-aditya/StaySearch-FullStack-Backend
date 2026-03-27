package com.example.StaySearchSystem.service;

import org.springframework.stereotype.Service;
import com.example.StaySearchSystem.entity.Booking;
import com.example.StaySearchSystem.entity.User;
import com.example.StaySearchSystem.entity.Role;
import com.example.StaySearchSystem.repository.BookingRepository;
import com.example.StaySearchSystem.repository.UserRepository;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepo;
    private final UserRepository userRepo;

    public BookingService(BookingRepository bookingRepo, UserRepository userRepo) {
        this.bookingRepo = bookingRepo;
        this.userRepo = userRepo;
    }

    public Booking createBooking(Booking booking) {
        booking.setStatus("PENDING");
        booking.setBookingCode("SS-" + System.currentTimeMillis());
        return bookingRepo.save(booking);
    }

    public Booking createBookingWithUser(Booking booking, Long userId) {
        if (userId != null) {
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            booking.setUser(user);
        }
        booking.setStatus("PENDING");
        booking.setBookingCode("SS-" + System.currentTimeMillis());
        return bookingRepo.save(booking);
    }

    public List<Booking> getUserBookings(Long userId) {
        return bookingRepo.findByUser_Id(userId);
    }

    // Admin can view all bookings
    public List<Booking> getAllBookings() {
        return bookingRepo.findAll();
    }

    // Check if user has permission to view booking
    public boolean canViewBooking(Long bookingId, Long userId, String userRole) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Admin can view any booking
        if ("ADMIN".equals(userRole)) {
            return true;
        }

        // User can only view their own bookings
        return booking.getUser() != null && booking.getUser().getId().equals(userId);
    }

    // Check if user can cancel booking
    public boolean canCancelBooking(Long bookingId, Long userId, String userRole) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Admin can cancel any booking
        if ("ADMIN".equals(userRole)) {
            return true;
        }

        // User can only cancel their own bookings
        return booking.getUser() != null && booking.getUser().getId().equals(userId);
    }

    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus("CANCELLED");
        bookingRepo.save(booking);
    }
}
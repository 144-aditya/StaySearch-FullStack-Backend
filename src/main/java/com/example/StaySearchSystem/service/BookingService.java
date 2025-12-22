package com.example.StaySearchSystem.service;

import org.springframework.stereotype.Service;
import com.example.StaySearchSystem.entity.Booking;
import com.example.StaySearchSystem.repository.BookingRepository;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepo;

    public BookingService(BookingRepository bookingRepo) {
        this.bookingRepo = bookingRepo;
    }

    public Booking createBooking(Booking booking) {
        booking.setStatus("PENDING");
        booking.setBookingCode("SS-" + System.currentTimeMillis());
        return bookingRepo.save(booking);
    }

    public List<Booking> getUserBookings(Long userId) {
        return bookingRepo.findByUser_Id(userId);
    }

    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus("CANCELLED");
        bookingRepo.save(booking);
    }
}

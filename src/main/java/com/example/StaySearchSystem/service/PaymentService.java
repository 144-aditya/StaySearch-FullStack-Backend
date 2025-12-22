package com.example.StaySearchSystem.service;

import org.springframework.stereotype.Service;
import com.example.StaySearchSystem.entity.Payment;
import com.example.StaySearchSystem.entity.Booking;
import com.example.StaySearchSystem.repository.PaymentRepository;
import com.example.StaySearchSystem.repository.BookingRepository;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepo;
    private final BookingRepository bookingRepo;

    public PaymentService(PaymentRepository paymentRepo, BookingRepository bookingRepo) {
        this.paymentRepo = paymentRepo;
        this.bookingRepo = bookingRepo;
    }

    public Payment savePayment(Payment payment) {
        Booking booking = bookingRepo.findById(payment.getBooking().getId()).orElseThrow();
        booking.setStatus("CONFIRMED");
        bookingRepo.save(booking);
        return paymentRepo.save(payment);
    }
}

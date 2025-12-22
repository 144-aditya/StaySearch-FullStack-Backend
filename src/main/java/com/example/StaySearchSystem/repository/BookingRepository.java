package com.example.StaySearchSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.StaySearchSystem.entity.Booking;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUser_Id(Long userId);
}

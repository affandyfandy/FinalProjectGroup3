package com.hotel.reservation_service.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.reservation_service.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Payment findByReservationId(UUID reservationId);  
}

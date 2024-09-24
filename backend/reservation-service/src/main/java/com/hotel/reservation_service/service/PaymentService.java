package com.hotel.reservation_service.service;

import com.hotel.reservation_service.entity.Payment;

import java.util.List;
import java.util.UUID;

public interface PaymentService {
    List<Payment> getPaymentsByReservationId(UUID reservationId);
    Payment createPayment(Payment payment, UUID reservationId);
}

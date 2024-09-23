package com.hotel.reservation_service.service.impl;

import com.hotel.reservation_service.entity.Payment;
import com.hotel.reservation_service.entity.Reservation;
import com.hotel.reservation_service.repository.PaymentRepository;
import com.hotel.reservation_service.repository.ReservationRepository;
import com.hotel.reservation_service.service.PaymentService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, ReservationRepository reservationRepository) {
        this.paymentRepository = paymentRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public List<Payment> getPaymentsByReservationId(UUID reservationId) {
        return paymentRepository.findByReservationId(reservationId);
    }

    @Override
    public Payment createPayment(Payment payment, UUID reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        payment.setReservation(reservation);
        return paymentRepository.save(payment);
    }
}

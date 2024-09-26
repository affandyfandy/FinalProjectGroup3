package com.hotel.reservation_service.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.hotel.reservation_service.entity.Payment;
import com.hotel.reservation_service.repository.PaymentRepository;
import com.hotel.reservation_service.service.PaymentService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment getPaymentsByReservationId(UUID reservationId) {
        return paymentRepository.findByReservationId(reservationId);  
    }

    @Override
    public Payment createPayment(Payment payment, UUID reservationId) {
        payment.setReservationId(reservationId); 
        return paymentRepository.save(payment);
    }
}

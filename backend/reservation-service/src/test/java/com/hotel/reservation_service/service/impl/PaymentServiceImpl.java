package com.hotel.reservation_service.service.impl;

import com.hotel.reservation_service.entity.Payment;
import com.hotel.reservation_service.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentServiceImpl;

    private Payment payment;
    private UUID reservationId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        payment = new Payment();
        payment.setId(UUID.randomUUID());
        reservationId = UUID.randomUUID();
    }

    @Test
    void getPaymentsByReservationId_ShouldReturnPayments() {
        when(paymentRepository.findByReservationId(reservationId)).thenReturn(List.of(payment));

        List<Payment> payments = paymentServiceImpl.getPaymentsByReservationId(reservationId);

        assertEquals(1, payments.size());
        assertEquals(payment.getId(), payments.get(0).getId());
        verify(paymentRepository, times(1)).findByReservationId(reservationId);
    }

    @Test
    void createPayment_ShouldReturnSavedPayment() {
        when(paymentRepository.save(payment)).thenReturn(payment);

        Payment savedPayment = paymentServiceImpl.createPayment(payment, reservationId);

        assertEquals(payment.getId(), savedPayment.getId());
        assertEquals(reservationId, savedPayment.getReservationId());
        verify(paymentRepository, times(1)).save(payment);
    }
}

package com.hotel.reservation_service.controller;

import com.hotel.reservation_service.entity.Payment;
import com.hotel.reservation_service.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/reservation/{reservationId}")
    public List<Payment> getPaymentsByReservation(@PathVariable UUID reservationId) {
        return paymentService.getPaymentsByReservationId(reservationId);
    }

    @PostMapping("/reservation/{reservationId}")
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment, @PathVariable UUID reservationId) {
        Payment createdPayment = paymentService.createPayment(payment, reservationId);
        return ResponseEntity.ok(createdPayment);
    }
}

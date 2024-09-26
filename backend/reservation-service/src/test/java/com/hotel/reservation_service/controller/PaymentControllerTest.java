package com.hotel.reservation_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.reservation_service.entity.Payment;
import com.hotel.reservation_service.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PaymentControllerTest {

	private MockMvc mockMvc;

	@Mock
	private PaymentService paymentService;

	@InjectMocks
	private PaymentController paymentController;

	private ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
	}

	@Test
	void testGetPaymentsByReservation() throws Exception {
		UUID reservationId = UUID.randomUUID();
		List<Payment> paymentList = new ArrayList<>();
		Payment payment = new Payment();
		payment.setId(UUID.randomUUID());
		payment.setAmount(BigDecimal.valueOf(150.00));
		paymentList.add(payment);

		when(paymentService.getPaymentsByReservationId(reservationId)).thenReturn(paymentList);

		mockMvc.perform(get("/api/v1/payments/reservation/" + reservationId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(payment.getId().toString()))
				.andExpect(jsonPath("$[0].amount").value(payment.getAmount()));
	}

	@Test
	void testCreatePayment() throws Exception {
		UUID reservationId = UUID.randomUUID();
		Payment payment = new Payment();
		payment.setId(UUID.randomUUID());
		payment.setAmount(BigDecimal.valueOf(150));

		when(paymentService.createPayment(any(Payment.class), any(UUID.class))).thenReturn(payment);

		mockMvc.perform(post("/api/v1/payments/reservation/" + reservationId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(payment)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(payment.getId().toString()))
				.andExpect(jsonPath("$.amount").value(payment.getAmount()));
	}
}

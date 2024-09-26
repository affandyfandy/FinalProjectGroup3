package com.hotel.reservation_service.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.reservation_service.entity.Reservation;
import com.hotel.reservation_service.entity.ReservationStatus;
import com.hotel.reservation_service.service.ReservationService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

class ReservationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reservationController).build();
    }

    @Test
    void testCreateReservation() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setId(UUID.randomUUID());
        reservation.setStatus(ReservationStatus.CANCELED);

        when(reservationService.createReservation(any(Reservation.class))).thenReturn(reservation);

        mockMvc.perform(post("/api/v1/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservation)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetReservationById() throws Exception {
        UUID reservationId = UUID.randomUUID();
        Reservation reservation = new Reservation();
        reservation.setId(reservationId);

        when(reservationService.getReservationById(reservationId)).thenReturn(reservation);

        mockMvc.perform(get("/api/v1/reservations/" + reservationId))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateReservation() throws Exception {
        UUID reservationId = UUID.randomUUID();
        Reservation reservation = new Reservation();
        reservation.setId(reservationId);

        when(reservationService.updateReservation(any(), any())).thenReturn(reservation);

        mockMvc.perform(put("/api/v1/reservations/" + reservationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservation)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteReservation() throws Exception {
        UUID reservationId = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/reservations/" + reservationId))
                .andExpect(status().isNoContent());
    }

}

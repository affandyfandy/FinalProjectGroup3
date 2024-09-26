package com.hotel.reservation_service.service.impl;

import com.hotel.reservation_service.entity.Reservation;
import com.hotel.reservation_service.entity.ReservationStatus;
import com.hotel.reservation_service.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.thymeleaf.TemplateEngine;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private TemplateEngine templateEngine;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUnavailableRoomIds() {
        List<UUID> roomIds = List.of(UUID.randomUUID());
        LocalDate checkInDate = LocalDate.now();
        LocalDate checkOutDate = checkInDate.plusDays(2);
        Page<Reservation> page = new PageImpl<>(List.of(new Reservation()));
        when(reservationRepository.findConflictingReservations(anyList(), any(), any(), any())).thenReturn(page);

        Page<UUID> result = reservationService.getUnavailableRoomIds(roomIds, checkInDate, checkOutDate, Pageable.unpaged());

        assertEquals(1, result.getContent().size());
        verify(reservationRepository, times(1)).findConflictingReservations(anyList(), any(), any(), any());
    }

    @Test
    void testGetAllReservations() {
        Page<Reservation> reservations = new PageImpl<>(List.of(new Reservation()));
        when(reservationRepository.findAll(any(Pageable.class))).thenReturn(reservations);

        Page<Reservation> result = reservationService.getAllReservations(Pageable.unpaged());

        assertEquals(reservations, result);
        verify(reservationRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testGetReservationById() {
        UUID id = UUID.randomUUID();
        Reservation reservation = new Reservation();
        when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));

        Reservation result = reservationService.getReservationById(id);

        assertEquals(reservation, result);
        verify(reservationRepository, times(1)).findById(id);
    }

    @Test
    void testCreateReservation() {
        Reservation reservation = new Reservation();
        when(reservationRepository.save(any())).thenReturn(reservation);

        Reservation result = reservationService.createReservation(reservation);

        assertEquals(reservation, result);
        verify(reservationRepository, times(1)).save(any());
    }

    @Test
    void testUpdateReservation() {
        UUID id = UUID.randomUUID();
        Reservation reservation = new Reservation();
        when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any())).thenReturn(reservation);

        Reservation result = reservationService.updateReservation(id, reservation);

        assertEquals(reservation, result);
        verify(reservationRepository, times(1)).save(any());
    }

    @Test
    void testDeleteReservation() {
        UUID id = UUID.randomUUID();
        Reservation reservation = new Reservation();
        when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));

        reservationService.deleteReservation(id);

        verify(reservationRepository, times(1)).delete(reservation);
    }

    @Test
    void testSearchReservations() {
        Page<Reservation> reservations = new PageImpl<>(List.of(new Reservation()));
        when(reservationRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(reservations);

        Page<Reservation> result = reservationService.searchReservations(ReservationStatus.CONFIRMED, "userId", LocalDateTime.now(), LocalDateTime.now(), Pageable.unpaged());

        assertEquals(reservations, result);
        verify(reservationRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
}

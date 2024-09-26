package com.hotel.reservation_service.controller;

import com.hotel.reservation_service.entity.Reservation;
import com.hotel.reservation_service.entity.ReservationStatus;
import com.hotel.reservation_service.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Sort;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUnavailableRoomIds() {
        List<UUID> roomIds = List.of(UUID.randomUUID());
        LocalDate checkInDate = LocalDate.now();
        LocalDate checkOutDate = checkInDate.plusDays(2);
        Page<UUID> roomIdsPage = new PageImpl<>(roomIds);
        when(reservationService.getUnavailableRoomIds(anyList(), any(), any(), any())).thenReturn(roomIdsPage);

        Page<UUID> result = reservationController.getUnavailableRoomIds(roomIds, checkInDate, checkOutDate, Pageable.unpaged());

        assertEquals(roomIdsPage, result);
        verify(reservationService, times(1)).getUnavailableRoomIds(anyList(), any(), any(), any());
    }

    @Test
    public void testGetAllReservations() {
        Page<Reservation> reservations = new PageImpl<>(List.of(new Reservation()));
        when(reservationService.getAllReservations(any(Pageable.class))).thenReturn(reservations);

        Page<Reservation> result = reservationController.getAllReservations(Pageable.unpaged());

        assertEquals(reservations, result);
        verify(reservationService, times(1)).getAllReservations(any(Pageable.class));
    }

    @Test
    public void testGetReservationById() {
        UUID id = UUID.randomUUID();
        Reservation reservation = new Reservation();
        when(reservationService.getReservationById(id)).thenReturn(reservation);

        Reservation result = reservationController.getReservationById(id);

        assertEquals(reservation, result);
        verify(reservationService, times(1)).getReservationById(id);
    }

    @Test
    public void testCreateReservation() {
        Reservation reservation = new Reservation();
        when(reservationService.createReservation(any())).thenReturn(reservation);

        Reservation result = reservationController.createReservation(reservation);

        assertEquals(reservation, result);
        verify(reservationService, times(1)).createReservation(any());
    }

    @Test
    public void testUpdateReservation() {
        UUID id = UUID.randomUUID();
        Reservation reservation = new Reservation();
        when(reservationService.updateReservation(any(), any())).thenReturn(reservation);

        Reservation result = reservationController.updateReservation(id, reservation);

        assertEquals(reservation, result);
        verify(reservationService, times(1)).updateReservation(any(), any());
    }

    @Test
    public void testDeleteReservation() {
        UUID id = UUID.randomUUID();

        ResponseEntity<Void> response = reservationController.deleteReservation(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(reservationService, times(1)).deleteReservation(id);
    }

    @Test
    public void testSearchReservations() {
        Page<Reservation> reservations = new PageImpl<>(List.of(new Reservation()));
        when(reservationService.searchReservations(any(), any(), any(), any(), any())).thenReturn(reservations);

        Page<Reservation> result = reservationController.searchReservations(ReservationStatus.CONFIRMED, "userId", LocalDateTime.now(), LocalDateTime.now(), Pageable.unpaged());

        assertEquals(reservations, result);
        verify(reservationService, times(1)).searchReservations(any(), any(), any(), any(), any());
    }

    @Test
    public void testGetAllReservationsSorted() {
        List<Reservation> reservations = List.of(new Reservation());
        when(reservationService.getAllReservations(any(Sort.class))).thenReturn(reservations);

        List<Reservation> result = reservationController.getAllReservationsSorted("reservationDate", Sort.Direction.DESC);

        assertEquals(reservations, result);
        verify(reservationService, times(1)).getAllReservations(any(Sort.class));
    }

}

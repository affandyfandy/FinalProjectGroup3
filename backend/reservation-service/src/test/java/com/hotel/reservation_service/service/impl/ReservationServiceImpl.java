package com.hotel.reservation_service.service.impl;

import com.hotel.reservation_service.entity.Reservation;
import com.hotel.reservation_service.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.thymeleaf.TemplateEngine;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private TemplateEngine templateEngine;

    @InjectMocks
    private ReservationServiceImpl reservationServiceImpl;

    private Reservation reservation;
    private UUID reservationId;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reservation = new Reservation();
        reservation.setId(UUID.randomUUID());
        reservationId = reservation.getId();
        pageable = Pageable.unpaged();
    }

    @Test
    void getUnavailableRoomIds_ShouldReturnRoomIds() {
        List<UUID> roomIds = List.of(UUID.randomUUID());
        Page<Reservation> reservationPage = new PageImpl<>(List.of(reservation));

        when(reservationRepository.findConflictingReservations(roomIds, LocalDate.now(), LocalDate.now().plusDays(2), pageable))
                .thenReturn(reservationPage);

        Page<UUID> result = reservationServiceImpl.getUnavailableRoomIds(roomIds, LocalDate.now(), LocalDate.now().plusDays(2), pageable);

        assertEquals(1, result.getContent().size());
        verify(reservationRepository, times(1)).findConflictingReservations(anyList(), any(), any(), any());
    }

    @Test
    void getAllReservations_ShouldReturnPageOfReservations() {
        Page<Reservation> reservationPage = new PageImpl<>(List.of(reservation));

        when(reservationRepository.findAll(pageable)).thenReturn(reservationPage);

        Page<Reservation> result = reservationServiceImpl.getAllReservations(pageable);

        assertEquals(1, result.getContent().size());
        verify(reservationRepository, times(1)).findAll(pageable);
    }

    @Test
    void getAllReservations_ShouldReturnSortedReservations() {
        Sort sort = Sort.unsorted();
        when(reservationRepository.findAll(sort)).thenReturn(List.of(reservation));

        List<Reservation> result = reservationServiceImpl.getAllReservations(sort);

        assertEquals(1, result.size());
        verify(reservationRepository, times(1)).findAll(sort);
    }

    @Test
    void getReservationById_ShouldReturnReservation() {
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        Reservation result = reservationServiceImpl.getReservationById(reservationId);

        assertEquals(reservation.getId(), result.getId());
        verify(reservationRepository, times(1)).findById(reservationId);
    }

    @Test
    void getReservationById_ShouldThrowExceptionWhenNotFound() {
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> reservationServiceImpl.getReservationById(reservationId));

        verify(reservationRepository, times(1)).findById(reservationId);
    }

    @Test
    void createReservation_ShouldReturnSavedReservation() {
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation result = reservationServiceImpl.createReservation(reservation);

        assertEquals(reservation.getId(), result.getId());
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void updateReservation_ShouldReturnUpdatedReservation() {
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation updatedReservation = reservationServiceImpl.updateReservation(reservationId, reservation);

        assertEquals(reservation.getId(), updatedReservation.getId());
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void deleteReservation_ShouldDeleteReservation() {
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        reservationServiceImpl.deleteReservation(reservationId);

        verify(reservationRepository, times(1)).delete(reservation);
    }
}

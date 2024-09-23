package com.hotel.reservation_service.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.hotel.reservation_service.entity.Reservation;

public interface ReservationService {
    Page<Reservation> getAllReservations(Pageable pageable);
    List<Reservation> getAllReservations(Sort sort);      
    Reservation getReservationById(UUID id);
    Reservation createReservation(Reservation reservation);
    Reservation updateReservation(UUID id, Reservation reservation);
    void deleteReservation(UUID id);
}
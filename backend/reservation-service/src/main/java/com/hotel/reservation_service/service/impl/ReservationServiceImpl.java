package com.hotel.reservation_service.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hotel.reservation_service.entity.Reservation;
import com.hotel.reservation_service.repository.ReservationRepository;
import com.hotel.reservation_service.service.ReservationService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Page<Reservation> getAllReservations(Pageable pageable) {
        return reservationRepository.findAll(pageable); 
    }

    @Override
    public List<Reservation> getAllReservations(Sort sort) {
        return reservationRepository.findAll(sort); 
    }

    @Override
    public Reservation getReservationById(UUID id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
    }

    @Override
    public Reservation createReservation(Reservation reservation) {
        reservation.setCreatedTime(LocalDateTime.now());
        reservation.setUpdatedTime(LocalDateTime.now());
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation updateReservation(UUID id, Reservation reservation) {
        Reservation existingReservation = getReservationById(id);
        existingReservation.setCheckInDate(reservation.getCheckInDate());
        existingReservation.setCheckOutDate(reservation.getCheckOutDate());
        existingReservation.setStatus(reservation.getStatus());
        existingReservation.setUpdatedTime(LocalDateTime.now());
        return reservationRepository.save(existingReservation);
    }

    @Override
    public void deleteReservation(UUID id) {
        Reservation reservation = getReservationById(id);
        reservationRepository.delete(reservation);
    }
}

package com.hotel.reservation_service.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hotel.reservation_service.entity.Reservation;
import com.hotel.reservation_service.entity.ReservationStatus;
import com.hotel.reservation_service.repository.ReservationRepository;
import com.hotel.reservation_service.service.ReservationService;
import com.hotel.reservation_service.specification.ReservationSpecification;

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
    public Page<Reservation> searchReservations(ReservationStatus status, String userId, LocalDateTime checkInDate, LocalDateTime checkOutDate, Pageable pageable) {
        Specification<Reservation> specification = Specification
            .where(ReservationSpecification.hasStatus(status))
            .and(ReservationSpecification.hasUserId(userId))
            .and(ReservationSpecification.hasCheckInDateAfter(checkInDate))
            .and(ReservationSpecification.hasCheckOutDateBefore(checkOutDate));

        return reservationRepository.findAll(specification, pageable);
    }

    @Override
    public Reservation getReservationById(UUID id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
    }

    @Override
    public Reservation createReservation(Reservation reservation) {
        reservation.setCreatedBy(null); 
        reservation.setUpdatedBy(null); 
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

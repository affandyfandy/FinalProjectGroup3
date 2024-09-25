package com.hotel.reservation_service.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotel.reservation_service.entity.Reservation;
import com.hotel.reservation_service.entity.ReservationStatus;

public interface ReservationRepository extends JpaRepository<Reservation, UUID>, JpaSpecificationExecutor<Reservation> {
    List<Reservation> findByUserId(String userId);

    @Query("SELECT r FROM Reservation r WHERE (:status IS NULL OR r.status = :status) AND (:userId IS NULL OR r.userId = :userId)")
    Page<Reservation> searchReservations(@Param("status") ReservationStatus status, @Param("userId") String userId, Pageable pageable);

    @Query("SELECT r FROM Reservation r WHERE r.roomId IN :roomIds AND (r.checkInDate <= :checkOutDate AND r.checkOutDate >= :checkInDate)")
    Page<Reservation> findConflictingReservations(@Param("roomIds") List<UUID> roomIds,
                                                  @Param("checkInDate") LocalDate checkInDate,
                                                  @Param("checkOutDate") LocalDate checkOutDate,
                                                  Pageable pageable);

}

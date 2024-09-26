package com.hotel.reservation_service.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.UUID;

import com.hotel.reservation_service.audit.Auditable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "reservations")
public class Reservation extends Auditable<String> {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private LocalDate reservationDate;

    @Column(nullable = false)
    private LocalDate checkInDate;

    @Column(nullable = false)
    private LocalDate checkOutDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private UUID roomId;

    @Column(nullable = true)
    private BigDecimal amount;

    @PrePersist
    public void prePersist() {
        this.reservationDate = LocalDate.now();
    }

}

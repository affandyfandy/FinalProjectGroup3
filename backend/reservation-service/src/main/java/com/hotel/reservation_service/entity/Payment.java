package com.hotel.reservation_service.entity;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "payments")
public class Payment extends AuditEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String accountName;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private UUID reservationId;
}

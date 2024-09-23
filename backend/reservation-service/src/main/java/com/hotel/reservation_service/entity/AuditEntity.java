package com.hotel.reservation_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class AuditEntity {

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdTime;

    @Column(nullable = false, updatable = false)
    private String createdBy;

    @Column(nullable = false)
    private LocalDateTime updatedTime;

    @Column(nullable = false)
    private String updatedBy;
}

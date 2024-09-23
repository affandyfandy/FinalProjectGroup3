package com.hotel.reservation_service.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

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

    @PrePersist
    public void prePersist() {
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedTime = LocalDateTime.now();
    }
}

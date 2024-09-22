package com.hotel.auth_service.entity;

import com.hotel.auth_service.audit.Auditable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TestEntity extends Auditable<String> {
    @Id
    private Long id;
    private String name;
}

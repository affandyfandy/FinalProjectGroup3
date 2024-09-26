package com.hotel.room_service.entity;

import java.math.BigDecimal;
import java.util.UUID;

import com.hotel.room_service.audit.Auditable;

import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rooms")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Room extends Auditable<String>{

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column
    private String roomNumber;

    @Column
    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    @Column
    private Integer capacity;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
    private BigDecimal price;

    @Lob
    @Column(name = "photo")
    private String photo;

    @ElementCollection(targetClass = Facility.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "room_facilities", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "facility", nullable = false)
    private List<Facility> facility;
}
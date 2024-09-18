package com.hotel.room_service.service;

import org.springframework.data.domain.Page;

import com.hotel.room_service.entity.Room;

import java.util.Optional;
import java.util.UUID;

public interface RoomService {
    Room create(Room room);
    Room update(UUID id, Room room);
    Page<Room> findAll(int pageNo, int pageSize);
    Optional<Room> findById(UUID id);
    Room updateStatus(UUID id, String status);
}

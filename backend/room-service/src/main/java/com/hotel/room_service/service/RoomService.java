package com.hotel.room_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hotel.room_service.entity.Room;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomService {
    Room create(Room room);
    Room update(UUID id, Room room);
    Page<Room> findAllSorted(int pageNo, int pageSize, String sortBy, String sortOrder);
    Page<Room> search(int pageNo, int pageSize, String status, String facility, String roomType, Integer capacity,
                BigDecimal lowerLimit, BigDecimal upperLimit);
    Room findById(UUID id);
    Page<Room> findAllActiveSorted(int pageNo, int pageSize, String sortBy, String sortOrder);
    Room updateStatus(UUID id, String status);
    Room updateRoom(UUID id, Room room);
    void deleteRoom(UUID id);
    void saveAll(List<Room> listRoom);

}

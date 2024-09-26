package com.hotel.room_service.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hotel.room_service.dto.response.ReadRoomDto;
import com.hotel.room_service.entity.Room;
@Service
public interface RoomService {
    Room create(Room room);
    Room update(UUID id, Room room);
    Page<Room> findAllSorted(int pageNo, int pageSize, String sortBy, String sortOrder);
    Page<Room> search(int pageNo, int pageSize, String status, String facility, String roomType, Integer capacity,
                BigDecimal lowerLimit);
    Room findById(UUID id);
    Page<Room> findAllActiveSorted(int pageNo, int pageSize, String sortBy, String sortOrder);
    Room updateStatus(UUID id, String status);
    Room updateRoom(UUID id, Room room);
    void deleteRoom(UUID id);
    void saveAll(List<Room> listRoom);

    Page<ReadRoomDto> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, int capacity, Pageable pageable);

    ReadRoomDto uploadRoomPhoto(UUID id, MultipartFile file);
}

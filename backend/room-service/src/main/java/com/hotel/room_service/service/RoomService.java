package com.hotel.room_service.service;

import com.hotel.room_service.criteria.RoomSearchCriteria;
import com.hotel.room_service.dto.response.ReadRoomDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.hotel.room_service.entity.Room;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomService {
    Room create(Room room);
    Room update(UUID id, Room room);
    Page<Room> findAllSorted(int pageNo, int pageSize, String sortBy, String sortOrder);
    // Page<Room> search(int pageNo, int pageSize, String roomNumber, Integer capacity, String roomType, BigDecimal price, String status);
    Page<Room> getAllRoomByQuery(int pageNo, int pageSize, RoomSearchCriteria criteria) ;
    Room findById(UUID id);
    Page<Room> findAllActiveSorted(int pageNo, int pageSize, String sortBy, String sortOrder);
    Room updateStatus(UUID id, String status);
    Room updateRoom(UUID id, Room room);
    void deleteRoom(UUID id);
    void saveAll(List<Room> listRoom);
    byte[] encoded(MultipartFile file) throws IOException;
    String byteToString(byte[] file);

    Page<Room> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, int capacity, int pageNo, int pageSize);

    ReadRoomDto uploadRoomPhoto(UUID id, MultipartFile file);
}

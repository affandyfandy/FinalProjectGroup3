package com.hotel.room_service.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hotel.room_service.data.model.Room;
import com.hotel.room_service.data.model.Status;
import com.hotel.room_service.data.repository.RoomRepository;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Override
    public Room create(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public Room update(UUID id, Room room) {
        Optional<Room> findRoom = findById(id);
        if (findRoom.isPresent()){
            Room updatedRoom = findRoom.get();
            updatedRoom.setCapacity(room.getCapacity());
            updatedRoom.setFacility(room.getFacility());
            updatedRoom.setRoomType(room.getRoomType());
            updatedRoom.setStatus(room.getStatus());
            roomRepository.save(updatedRoom);
        }
        return findRoom.get();
    }

    @Override
    public Optional<Room> findById(UUID id) {
        return roomRepository.findById(id);
    }

    @Override
    public Room updateStatus(UUID id, String status) {
        Optional<Room> findRoom = findById(id);
        if (findRoom.isPresent()){
            Room getRoom = findRoom.get();
            if (status.equals("active")){
                getRoom.setStatus(Status.ACTIVE);
            }
            else if (status.equals("inactive")){
                getRoom.setStatus(Status.INACTIVE);
            }
        }
        return findRoom.get();
    }

    @Override
    public Page<Room> findAll(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return roomRepository.findAll(pageable);
    }
    
}

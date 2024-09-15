package com.hotel.room_service.data.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotel.room_service.data.model.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID>{
    
}

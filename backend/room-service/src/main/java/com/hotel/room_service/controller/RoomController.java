package com.hotel.room_service.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.room_service.data.model.Room;
import com.hotel.room_service.dto.RoomMapper;
import com.hotel.room_service.dto.request.CreateRoomDto;
import com.hotel.room_service.dto.response.ReadRoomDto;
import com.hotel.room_service.service.RoomService;

@RestController
@RequestMapping("/api/v1/room")
public class RoomController {

    public RoomService roomService;
    public RoomMapper roomMapper;

    public RoomController(RoomService roomService, RoomMapper roomMapper){
        this.roomService = roomService;
        this.roomMapper = roomMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomDetails(@PathVariable UUID id){
        Room room = roomService.findById(id).get() != null ? roomService.findById(id).get() : null;
        ReadRoomDto roomDto = roomMapper.toDto(room);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(roomDto);
        
    }   

    @PostMapping
    public ResponseEntity<?> createNewRoom(@RequestBody CreateRoomDto newRoom){
        Room room = roomMapper.toEntity(newRoom);
        ReadRoomDto roomDto = roomMapper.toDto(roomService.create(room));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(roomDto);
    }

    @GetMapping
    public ResponseEntity<?> findAllRooms(
        @RequestParam(defaultValue = "0", required = false) int pageNo,
        @RequestParam(defaultValue = "10", required = false) int pageSize) 
    {
        Page<Room> listRoom = roomService.findAll(pageNo, pageSize);
        List<ReadRoomDto> listRoomDto = roomMapper.toListDto(listRoom.getContent());
        Page<ReadRoomDto> pageRoomDto = new PageImpl<>(listRoomDto, listRoom.getPageable(), listRoom.getTotalElements());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(pageRoomDto);
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<?> activateRoomStatus(@PathVariable UUID id){
        Room updateRoom = roomService.updateStatus(id, "active");
        ReadRoomDto updateRoomDto = roomMapper.toDto(updateRoom);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updateRoomDto);
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateRoomStatus(@PathVariable UUID id){
        Room updateRoom = roomService.updateStatus(id, "inactive");
        ReadRoomDto updateRoomDto = roomMapper.toDto(updateRoom);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updateRoomDto);
    }
}

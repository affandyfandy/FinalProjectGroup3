package com.hotel.room_service.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.room_service.dto.RoomMapper;
import com.hotel.room_service.dto.request.CreateRoomDto;
import com.hotel.room_service.dto.response.ReadRoomDto;
import com.hotel.room_service.entity.Room;
import com.hotel.room_service.exception.InvalidInputException;
import com.hotel.room_service.service.RoomService;

@RestController
@RequestMapping("/api/v1/room")
public class RoomController {

    private static final Logger log = LoggerFactory.getLogger(RoomController.class);

    public RoomService roomService;
    public RoomMapper roomMapper;

    public RoomController(RoomService roomService, RoomMapper roomMapper){
        this.roomService = roomService;
        this.roomMapper = roomMapper;
    }

    @GetMapping("/available")
    public Page<ReadRoomDto> getAvailableRooms(@RequestParam LocalDate checkIn,
                                               @RequestParam LocalDate checkOut,
                                               @RequestParam(required = false, defaultValue = "1") int capacity,
                                               Pageable pageable) {
        return roomService.getAvailableRooms(checkIn, checkOut, capacity, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomDetails(@PathVariable UUID id){
        Room room = roomService.findById(id) != null ? roomService.findById(id) : null;
        ReadRoomDto roomDto = roomMapper.toDto(room);
        roomDto.setPhotoUrl(roomService.byteToString(room.getPhoto()));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(roomDto);
    }   

    @PostMapping("/create")
    public ResponseEntity<?> createNewRoom(
        @RequestParam("roomData") String roomDataJson,
        @RequestParam(value="image", required = false) MultipartFile multipartFile) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        CreateRoomDto newRoom = objectMapper.readValue(roomDataJson, CreateRoomDto.class);

        byte[] photo = null;
        if (multipartFile != null && !multipartFile.isEmpty()) {
            photo = roomService.encoded(multipartFile);
        }
        Room room = roomMapper.toEntity(newRoom);
        if (photo != null) {
            room.setPhoto(photo);
        }
        ReadRoomDto roomDto = roomMapper.toDto(roomService.create(room));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(roomDto);
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<?> activateRoomStatus(@PathVariable UUID id){
        Room updateRoom = roomService.updateStatus(id, "active");
        ReadRoomDto updateRoomDto = roomMapper.toDto(updateRoom);
        log.info("Room is ACTIVE");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updateRoomDto);
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateRoomStatus(@PathVariable UUID id){
        Room updateRoom = roomService.updateStatus(id, "inactive");
        ReadRoomDto updateRoomDto = roomMapper.toDto(updateRoom);
        log.info("Room is INACTIVE");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updateRoomDto);
    }

    @GetMapping
    public ResponseEntity<?> findAllSorted(
        @RequestParam(defaultValue = "0", required = false) int pageNo,
        @RequestParam(defaultValue = "10", required = false) int pageSize,
        @RequestParam(defaultValue = "roomType", required = false) String sortBy,
        @RequestParam(defaultValue = "asc", required = false) String sortOrder)
    {
        Page<Room> listRoom = roomService.findAllSorted(pageNo, pageSize, sortBy, sortOrder);
        List<ReadRoomDto> listRoomDto = roomMapper.toListDto(listRoom.getContent());
        Page<ReadRoomDto> pageRoomDto = new PageImpl<>(listRoomDto, listRoom.getPageable(), listRoom.getTotalElements());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(pageRoomDto);
    }

    @GetMapping("/active")
    public ResponseEntity<?> findAllActiveRooms(
        @RequestParam(defaultValue = "0", required = false) int pageNo,
        @RequestParam(defaultValue = "10", required = false) int pageSize,
        @RequestParam(defaultValue = "price", required = false) String sortBy,
        @RequestParam(defaultValue = "asc", required = false) String sortOrder)
    {
        Page<Room> listRoom = roomService.findAllActiveSorted(pageNo, pageSize, sortBy, sortOrder);
        List<ReadRoomDto> listRoomDto = roomMapper.toListDto(listRoom.getContent());
        Page<ReadRoomDto> pageRoomDto = new PageImpl<>(listRoomDto, listRoom.getPageable(), listRoom.getTotalElements());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(pageRoomDto);
    }

    @GetMapping("/search")
    public ResponseEntity<?> filterRooms(
        @RequestParam(defaultValue = "0", required = false) int pageNo,
        @RequestParam(defaultValue = "10", required = false) int pageSize,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String facility,
        @RequestParam(required = false) Integer capacity,
        @RequestParam(required = false) String roomType,
        @RequestParam(required = false) BigDecimal lowerLimitPrice,
        @RequestParam(required = false) BigDecimal upperLimitPrice
        )
    {
        Page<Room> listRoom = roomService.search(pageNo, pageSize, status, facility, roomType, capacity, lowerLimitPrice, upperLimitPrice);
        List<ReadRoomDto> listRoomDto = roomMapper.toListDto(listRoom.getContent());
        Page<ReadRoomDto> pageRoomDto = new PageImpl<>(listRoomDto, listRoom.getPageable(), listRoom.getTotalElements());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(pageRoomDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editRoomDetails(@RequestBody CreateRoomDto dto, @PathVariable("id") UUID id){
        Room updateRoom = roomService.updateRoom(id, roomMapper.toEntity(dto));
        ReadRoomDto readRoomDto = roomMapper.toDto(updateRoom);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(readRoomDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable("id") UUID id){
        roomService.deleteRoom(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Collections.singletonMap("status", "success"));
    }

    @PostMapping("/import")
    public ResponseEntity<String> importProduct(@RequestParam("file") MultipartFile file){
        try {
            // List<Room> listProduct = FileUtils.readProductFromExcel(file);
            List<Room> listProduct = new ArrayList<>();
            roomService.saveAll(listProduct);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Room imported successfully");
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file content: " + e.getMessage());
        }
    }

}

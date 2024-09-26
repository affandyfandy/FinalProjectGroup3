package com.hotel.room_service.controller;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.hotel.room_service.criteria.RoomSearchCriteria;
import com.hotel.room_service.dto.RoomMapper;
import com.hotel.room_service.dto.request.CreateRoomDto;
import com.hotel.room_service.dto.response.ReadRoomDto;
import com.hotel.room_service.entity.Room;
import com.hotel.room_service.exception.InvalidInputException;
import com.hotel.room_service.service.RoomService;

import feign.Response;

@RestController
@RequestMapping("/api/v1/room")
public class RoomController {

    private static final Logger log = LoggerFactory.getLogger(RoomController.class);
    private final Path rootLocation = Paths.get("src/main/resources/static/images");

    public RoomService roomService;
    public RoomMapper roomMapper;

    public RoomController(RoomService roomService, RoomMapper roomMapper){
        this.roomService = roomService;
        this.roomMapper = roomMapper;
    }

    @GetMapping("/available")
    public ResponseEntity<?> getAvailableRooms(@RequestParam(required = false, defaultValue = "") LocalDate checkIn,
                                               @RequestParam(required = false, defaultValue = "") LocalDate checkOut,
                                               @RequestParam(required = false, defaultValue = "1") int capacity,
                                               @RequestParam(defaultValue = "0", required = false) int pageNo,
                                               @RequestParam(defaultValue = "10", required = false) int pageSize) {
        Page<Room> pageRoom = roomService.getAvailableRooms(checkIn, checkOut, capacity, pageNo, pageSize);
        List<ReadRoomDto> listRoomDto = roomMapper.toListDto(pageRoom.getContent());
        Page<ReadRoomDto> pageRoomDto = new PageImpl<>(listRoomDto, pageRoom.getPageable(), pageRoom.getTotalElements());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(pageRoomDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomDetails(@PathVariable UUID id){
        Room room = roomService.findById(id) != null ? roomService.findById(id) : null;
        ReadRoomDto roomDto = roomMapper.toDto(room);
//        roomDto.setPhotoUrl(roomService.byteToString(room.getPhoto()));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(roomDto);
    }   

    @PostMapping("/create")
    public ResponseEntity<?> createNewRoom(
        @RequestPart("roomData") CreateRoomDto roomDto) {

        Room room = roomService.create(roomMapper.toEntity(roomDto));
        ReadRoomDto readRoomDto = roomMapper.toDto(room);

        return ResponseEntity.status(HttpStatus.CREATED).body(readRoomDto);
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
        RoomSearchCriteria criteria)
    {
        Page<Room> listRoom = roomService.getAllRoomByQuery(pageNo, pageSize, criteria);
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

    @PostMapping("/{id}/photo")
    public ResponseEntity<?> uploadUserPhoto(@PathVariable("id") UUID id, @RequestParam("file") MultipartFile file) {
        try {
            ReadRoomDto updatedUser = roomService.uploadRoomPhoto(id, file);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<?> getImage(@PathVariable String filename) {
        try {
            Path filePath = rootLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
        }
    }

    @GetMapping("/list/all")
    public List<ReadRoomDto> getAllRoomsInList(){
        List<Room> listRoom = roomService.findAll();
        return roomMapper.toListDto(listRoom);
    }
}

package com.hotel.room_service.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.Base64;

import com.hotel.room_service.client.ReservationServiceClient;
import com.hotel.room_service.criteria.RoomSearchCriteria;
import com.hotel.room_service.dto.RoomMapper;
import com.hotel.room_service.dto.response.ReadRoomDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hotel.room_service.entity.Room;
import com.hotel.room_service.entity.Status;
import com.hotel.room_service.repository.RoomRepository;
import com.hotel.room_service.specification.RoomSpecification;


@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ReservationServiceClient reservationServiceClient;
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public Room create(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public Room update(UUID id, Room room) {
        Room findRoom = findById(id);
        if (findRoom != null){
            findRoom.setCapacity(room.getCapacity());
            findRoom.setFacility(room.getFacility());
            findRoom.setRoomType(room.getRoomType());
            findRoom.setStatus(room.getStatus());
            roomRepository.save(findRoom);
        }
        return findRoom;
    }

    @Override
    public Room findById(UUID id) {
        return roomRepository.findById(id).get();
    }

    @Override
    public Room updateStatus(UUID id, String status) {
        Room findRoom = findById(id);
        if (findRoom != null){
            if (status.equals("active")){
                findRoom.setStatus(Status.ACTIVE);
            }
            else if (status.equals("inactive")){
                findRoom.setStatus(Status.INACTIVE);
            }
            roomRepository.save(findRoom);
        }
        return findRoom;
    }

    @Override
    public Page<Room> findAllSorted(int pageNo, int pageSize, String sortBy, String sortOrder) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction,sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return roomRepository.findAll(pageable);
    }

    @Override
    public Page<Room> getAllRoomByQuery(int pageNo, int pageSize, RoomSearchCriteria criteria) {
        Sort.Direction direction = Sort.Direction.ASC;
        Sort sort = Sort.by(direction,"price");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        RoomSpecification roomSpecification = new RoomSpecification(criteria);
        return roomRepository.findAll(roomSpecification, pageable);
    }

    // @Override
    // public Page<Room> search(int pageNo, int pageSize, String roomNumber, Integer capacity, String roomType, BigDecimal price, String status) {
    //     Sort sort = Sort.by(Sort.Direction.ASC,"price");
    //     Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

    //     // Search by status only
    //     if (status != null) {
    //         return roomRepository.findAllByStatus(status, pageable);
    //     }

    //     // // Search by status and facility
    //     // if (status != null && facility != null && roomType == null && capacity == null && lowerLimit == null) {
    //     //     return roomRepository.findAllByStatusAndFacility(status, facility, pageable);
    //     // }

    //     // // Search by status and roomType
    //     // if (status != null && roomType != null && facility == null && capacity == null && lowerLimit == null) {
    //     //     return roomRepository.findAllByStatusAndRoomType(status, roomType, pageable);
    //     // }

    //     // // Search by status and capacity
    //     // if (status != null && capacity != null && facility == null && roomType == null && lowerLimit == null) {
    //     //     return roomRepository.findAllByStatusAndCapacity(status, capacity, pageable);
    //     // }

    //     // // Search by status, lowerLimit, an (for price range)
    //     // if (status != null && lowerLimit != null && facility == null && roomType == null && capacity == null) {
    //     //     return roomRepository.findAllByStatusAndPriceRange(status, lowerLimit, pageable);
    //     // }

    //     // // Search by status, facility, and roomType
    //     // if (status != null && facility != null && roomType != null && capacity == null && lowerLimit == null) {
    //     //     return roomRepository.findAllByStatusAndFacilityAndRoomType(status, facility, roomType, pageable);
    //     // }

    //     // // Search by status, facility, and capacity
    //     // if (status != null && facility != null && capacity != null && roomType == null && lowerLimit == null) {
    //     //     return roomRepository.findAllByStatusAndFacilityAndCapacity(status, facility, capacity, pageable);
    //     // }

    //     // // Search by status, facility, and price range
    //     // if (status != null && facility != null && lowerLimit != null && roomType == null && capacity == null) {
    //     //     return roomRepository.findAllByStatusAndFacilityAndPriceRange(status, facility, lowerLimit, pageable);
    //     // }

    //     // // Search by status, facility, roomType, and capacity
    //     // if (status != null && facility != null && roomType != null && capacity != null && lowerLimit == null) {
    //     //     return roomRepository.findAllByStatusAndFacilityAndRoomTypeAndCapacity(status, facility, roomType, capacity, pageable);
    //     // }

    //     // // Search by status, facility, roomType, and price range
    //     // if (status != null && facility != null && roomType != null && lowerLimit != null && capacity == null) {
    //     //     return roomRepository.findAllByStatusAndFacilityAndRoomTypeAndPriceRange(status, facility, roomType, lowerLimit, pageable);
    //     // }

    //     // // Search by status, roomType, and price range
    //     // if (status != null && roomType != null && lowerLimit != null && facility == null && capacity == null) {
    //     //     return roomRepository.findAllByStatusAndRoomTypeAndPriceRange(status, roomType, lowerLimit, pageable);
    //     // }

    //     // // Search by status, roomType, and capacity
    //     // if (status != null && roomType != null && capacity != null && facility == null && lowerLimit == null) {
    //     //     return roomRepository.findAllByStatusAndRoomTypeAndCapacity(status, roomType, capacity, pageable);
    //     // }

    //     // // Search by status, roomType, capacity, and price range
    //     // if (status != null && roomType != null && capacity != null && lowerLimit != null && facility == null) {
    //     //     return roomRepository.findAllByStatusAndRoomTypeAndCapacityAndPriceRange(status, roomType, capacity, lowerLimit, pageable);
    //     // }

    //     // If no filters match, return all rooms
    //     return roomRepository.findAll(pageable);
    // }

    @Override
    public Page<Room> findAllActiveSorted(int pageNo, int pageSize, String sortBy, String sortOrder) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction,sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return roomRepository.findAllActiveRooms(pageable);
    }

    @Override
    public Room updateRoom(UUID id, Room room){
        Room findRoom = findById(id);
        if (findRoom != null){
            findRoom.setCapacity(room.getCapacity());
            findRoom.setFacility(room.getFacility());
            findRoom.setPrice(room.getPrice());
            findRoom.setRoomNumber(room.getRoomNumber());
            findRoom.setRoomType(room.getRoomType());
            findRoom.setPhoto(room.getPhoto());
            roomRepository.save(findRoom);
        }
        return findRoom;
    }
    @Override
    public void deleteRoom(UUID id) {
        Room findRoom = findById(id);
        roomRepository.delete(findRoom);
    }

    @Override
    public void saveAll(List<Room> listRoom) {
        roomRepository.saveAll(listRoom);
    }

    @Override
    public byte[] encoded(MultipartFile file) throws IOException {
        return file.getBytes();
        // return Base64.getEncoder().encodeToString(imageBytes);
    }

    @Override
    public String byteToString(byte[] file) {
        return Base64.getEncoder().encodeToString(file);
    }

    public Page<Room> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, int capacity, int pageNo, int pageSize){
        Sort.Direction direction = Sort.Direction.ASC;
        Sort sort = Sort.by(direction,"price");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Room> activeRooms = roomRepository.findAllActiveRoomsAndCapacityGreaterThanEqual(capacity, pageable);
        // List<UUID> roomIds = activeRooms.stream().map(Room::getId).toList();
        // Page<UUID> unavailableRoomIds = reservationServiceClient.getUnavailableRoomIds(roomIds, checkInDate, checkOutDate, pageable);

        // List<ReadRoomDto> availableRooms = activeRooms.stream()
        //         .filter(room -> !unavailableRoomIds.getContent().contains(room.getId()))
        //         .map(roomMapper::toDto)
        //         .toList();

        return activeRooms;
    }

    @Override
    public ReadRoomDto uploadRoomPhoto(UUID id, MultipartFile file) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        if (room.getPhoto() != null && room.getPhoto().startsWith("/images/")) {
            fileStorageService.deleteFile(room.getPhoto().substring(8));
        }

        String fileName = fileStorageService.storeFile(file);
        room.setPhoto("/images/" + fileName);

        return roomMapper.toDto(roomRepository.save(room));
    }
}

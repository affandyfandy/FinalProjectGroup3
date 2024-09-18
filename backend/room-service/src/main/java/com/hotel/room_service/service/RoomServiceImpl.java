package com.hotel.room_service.service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hotel.room_service.entity.Room;
import com.hotel.room_service.entity.Status;
import com.hotel.room_service.repository.RoomRepository;


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
    public Page<Room> findAllSorted(int pageNo, int pageSize, String sortBy, String sortOrder) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction,sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return roomRepository.findAll(pageable);
    }

    @Override
    public Page<Room> search(int pageNo, int pageSize, String status, String facility, String roomType, Integer capacity, BigDecimal lowerLimit,
            BigDecimal upperLimit) {
            Pageable pageable = PageRequest.of(pageNo, pageSize);
        // Search by status only
        if (status != null && facility == null && roomType == null && capacity == null && lowerLimit == null && upperLimit == null) {
            return roomRepository.findAllByStatus(status, pageable);
        }

        // Search by status and facility
        if (status != null && facility != null && roomType == null && capacity == null && lowerLimit == null && upperLimit == null) {
            return roomRepository.findAllByStatusAndFacility(status, facility, pageable);
        }

        // Search by status and roomType
        if (status != null && roomType != null && facility == null && capacity == null && lowerLimit == null && upperLimit == null) {
            return roomRepository.findAllByStatusAndRoomType(status, roomType, pageable);
        }

        // Search by status and capacity
        if (status != null && capacity != null && facility == null && roomType == null && lowerLimit == null && upperLimit == null) {
            return roomRepository.findAllByStatusAndCapacity(status, capacity, pageable);
        }

        // Search by status, lowerLimit, and upperLimit (for price range)
        if (status != null && lowerLimit != null && upperLimit != null && facility == null && roomType == null && capacity == null) {
            return roomRepository.findAllByStatusAndPriceRange(status, lowerLimit, upperLimit, pageable);
        }

        // Search by status, facility, and roomType
        if (status != null && facility != null && roomType != null && capacity == null && lowerLimit == null && upperLimit == null) {
            return roomRepository.findAllByStatusAndFacilityAndRoomType(status, facility, roomType, pageable);
        }

        // Search by status, facility, and capacity
        if (status != null && facility != null && capacity != null && roomType == null && lowerLimit == null && upperLimit == null) {
            return roomRepository.findAllByStatusAndFacilityAndCapacity(status, facility, capacity, pageable);
        }

        // Search by status, facility, and price range
        if (status != null && facility != null && lowerLimit != null && upperLimit != null && roomType == null && capacity == null) {
            return roomRepository.findAllByStatusAndFacilityAndPriceRange(status, facility, lowerLimit, upperLimit, pageable);
        }

        // Search by status, facility, roomType, and capacity
        if (status != null && facility != null && roomType != null && capacity != null && lowerLimit == null && upperLimit == null) {
            return roomRepository.findAllByStatusAndFacilityAndRoomTypeAndCapacity(status, facility, roomType, capacity, pageable);
        }

        // Search by status, facility, roomType, and price range
        if (status != null && facility != null && roomType != null && lowerLimit != null && upperLimit != null && capacity == null) {
            return roomRepository.findAllByStatusAndFacilityAndRoomTypeAndPriceRange(status, facility, roomType, lowerLimit, upperLimit, pageable);
        }

        // Search by status, roomType, and price range
        if (status != null && roomType != null && lowerLimit != null && upperLimit != null && facility == null && capacity == null) {
            return roomRepository.findAllByStatusAndRoomTypeAndPriceRange(status, roomType, lowerLimit, upperLimit, pageable);
        }

        // Search by status, roomType, and capacity
        if (status != null && roomType != null && capacity != null && facility == null && lowerLimit == null && upperLimit == null) {
            return roomRepository.findAllByStatusAndRoomTypeAndCapacity(status, roomType, capacity, pageable);
        }

        // Search by status, roomType, capacity, and price range
        if (status != null && roomType != null && capacity != null && lowerLimit != null && upperLimit != null && facility == null) {
            return roomRepository.findAllByStatusAndRoomTypeAndCapacityAndPriceRange(status, roomType, capacity, lowerLimit, upperLimit, pageable);
        }

        // If no filters match, return all rooms
        return roomRepository.findAll(pageable);
    }
}

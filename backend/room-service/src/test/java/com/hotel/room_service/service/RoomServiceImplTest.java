package com.hotel.room_service.service;

import java.util.Arrays;
import java.util.UUID;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.hotel.room_service.entity.Facility;
import com.hotel.room_service.entity.Room;
import com.hotel.room_service.entity.RoomType;
import com.hotel.room_service.entity.Status;
import com.hotel.room_service.repository.RoomRepository;

public class RoomServiceImplTest {
    @InjectMocks
    private RoomServiceImpl roomService;

    @Mock
    private RoomRepository roomRepository;

    private Room room;
    private UUID roomId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        roomId = UUID.randomUUID();
        room = new Room();
        room.setId(roomId);
        room.setRoomNumber("DO9000");
        room.setRoomType(RoomType.SUITE);
        room.setCapacity(5);
        room.setPrice(BigDecimal.valueOf(100001000));
        room.setStatus(Status.ACTIVE);
    }

    @Test
    void testCreateRoom() {
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        Room result = roomService.create(room);

        assertNotNull(result);
        assertEquals(room, result);
        verify(roomRepository, times(1)).save(room);
    }
    @Test
    void testUpdateRoom() {
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        Room updatedRoom = new Room();
        updatedRoom.setCapacity(10);
        updatedRoom.setFacility(Arrays.asList(Facility.HAIR_DRYER));
        updatedRoom.setRoomType(RoomType.SINGLE);

        Room result = roomService.update(roomId, updatedRoom);

        assertNotNull(result);
        assertEquals(roomId, result.getId());
        assertEquals(10, result.getCapacity());
        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void testFindById() {
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));

        Room result = roomService.findById(roomId);

        assertNotNull(result);
        assertEquals(roomId, result.getId());
        verify(roomRepository, times(1)).findById(roomId);
    }

    @Test
    void testUpdateStatus() {
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        Room result = roomService.updateStatus(roomId, "active");

        assertNotNull(result);
        assertEquals(Status.ACTIVE, result.getStatus());
        verify(roomRepository, times(1)).save(room);
    }
    
}

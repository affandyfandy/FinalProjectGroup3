package com.hotel.room_service.service.Impl;

import com.hotel.room_service.client.ReservationServiceClient;
import com.hotel.room_service.dto.RoomMapper;
import com.hotel.room_service.dto.response.ReadRoomDto;
import com.hotel.room_service.entity.Room;
import com.hotel.room_service.entity.RoomType;
import com.hotel.room_service.entity.Status;
import com.hotel.room_service.repository.RoomRepository;
import com.hotel.room_service.service.FileStorageService;
import com.hotel.room_service.service.RoomServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RoomServiceImplTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ReservationServiceClient reservationServiceClient;

    @Mock
    private RoomMapper roomMapper;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private RoomServiceImpl roomService;

    private Room room;
    private UUID roomId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        roomId = UUID.randomUUID();
        room = new Room();
        room.setId(roomId);
        room.setRoomNumber("101");
        room.setCapacity(2);
        room.setRoomType(RoomType.SINGLE);
        room.setStatus(Status.ACTIVE);
    }

    @Test
    void testCreateRoom_Success() {
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        Room createdRoom = roomService.create(room);

        assertNotNull(createdRoom);
        assertEquals(room.getRoomNumber(), createdRoom.getRoomNumber());
        verify(roomRepository, times(1)).save(room);
    }

    @Test
    void testUpdateRoom_Success() {
        when(roomRepository.findById(any(UUID.class))).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        Room updatedRoom = roomService.update(roomId, room);

        assertNotNull(updatedRoom);
        verify(roomRepository, times(1)).save(room);
        verify(roomRepository, times(1)).findById(roomId);
    }

    @Test
    void testUpdateRoom_NotFound() {
        when(roomRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            roomService.updateRoom(roomId, room);
        });

        verify(roomRepository, times(1)).findById(roomId);
        verify(roomRepository, never()).save(any(Room.class));
    }


    @Test
    void testFindById_Success() {
        when(roomRepository.findById(any(UUID.class))).thenReturn(Optional.of(room));

        Room foundRoom = roomService.findById(roomId);

        assertNotNull(foundRoom);
        assertEquals(roomId, foundRoom.getId());
        verify(roomRepository, times(1)).findById(roomId);
    }

    @Test
    void testFindById_NotFound() {
        when(roomRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        Room foundRoom = roomService.findById(roomId);

        assertNull(foundRoom);
        verify(roomRepository, times(1)).findById(roomId);
    }


    @Test
    void testUpdateStatus_Success() {
        when(roomRepository.findById(any(UUID.class))).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        Room updatedRoom = roomService.updateStatus(roomId, "active");

        assertEquals(Status.ACTIVE, updatedRoom.getStatus());
        verify(roomRepository, times(1)).save(room);
    }

    @Test
    void testUpdateStatus_ValidStatusActive() {
        when(roomRepository.findById(any(UUID.class))).thenReturn(Optional.of(room));

        // Simulasi update dengan status valid "active"
        Room updatedRoom = roomService.updateStatus(roomId, "active");

        // Status seharusnya berubah menjadi ACTIVE
        assertEquals(Status.ACTIVE, updatedRoom.getStatus());
        verify(roomRepository, times(1)).save(updatedRoom); // Verifikasi bahwa save dipanggil
    }

    @Test
    void testUpdateStatus_ValidStatusInactive() {
        when(roomRepository.findById(any(UUID.class))).thenReturn(Optional.of(room));

        // Simulasi update dengan status valid "inactive"
        Room updatedRoom = roomService.updateStatus(roomId, "inactive");

        // Status seharusnya berubah menjadi INACTIVE
        assertEquals(Status.INACTIVE, updatedRoom.getStatus());
        verify(roomRepository, times(1)).save(updatedRoom); // Verifikasi bahwa save dipanggil
    }

    @Test
    void testUpdateStatus_InvalidStatus() {
        when(roomRepository.findById(any(UUID.class))).thenReturn(Optional.of(room));

        // Simulasi update dengan status tidak valid "invalid-status"
        Room updatedRoom = roomService.updateStatus(roomId, "invalid-status");

        // Status seharusnya tidak berubah (tetap ACTIVE, sesuai setup awal)
        assertEquals(Status.ACTIVE, updatedRoom.getStatus()); // Status tetap sama karena tidak valid
        verify(roomRepository, times(0)).save(room); // Tidak ada penyimpanan karena status tidak valid
    }


    @Test
    void testFindAllSorted_Success() {
        Page<Room> roomPage = new PageImpl<>(List.of(room));
        when(roomRepository.findAll(any(Pageable.class))).thenReturn(roomPage);

        Page<Room> result = roomService.findAllSorted(0, 10, "roomNumber", "asc");

        assertEquals(1, result.getTotalElements());
        verify(roomRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testFindAllActiveSorted_Success() {
        Page<Room> roomPage = new PageImpl<>(List.of(room));
        when(roomRepository.findAllActiveRooms(any(Pageable.class))).thenReturn(roomPage);

        Page<Room> result = roomService.findAllActiveSorted(0, 10, "roomNumber", "asc");

        assertEquals(1, result.getTotalElements());
        verify(roomRepository, times(1)).findAllActiveRooms(any(Pageable.class));
    }

    @Test
    void testSearchByStatus_Success() {
        Page<Room> roomPage = new PageImpl<>(List.of(room));
        when(roomRepository.findAllByStatus(anyString(), any(Pageable.class))).thenReturn(roomPage);

        Page<Room> result = roomService.search(0, 10, "active", null, null, null, null);

        assertEquals(1, result.getTotalElements());
        verify(roomRepository, times(1)).findAllByStatus(anyString(), any(Pageable.class));
    }

    @Test
    void testDeleteRoom_Success() {
        when(roomRepository.findById(any(UUID.class))).thenReturn(Optional.of(room));
        doNothing().when(roomRepository).delete(any(Room.class));

        roomService.deleteRoom(roomId);

        verify(roomRepository, times(1)).findById(roomId);
        verify(roomRepository, times(1)).delete(room);
    }

    @Test
    void testDeleteRoom_NotFound() {
        when(roomRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            roomService.deleteRoom(roomId);
        });

        verify(roomRepository, times(1)).findById(roomId);
        verify(roomRepository, never()).delete(any(Room.class));
    }


    @Test
    void testUploadRoomPhoto_Success() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(roomRepository.findById(any(UUID.class))).thenReturn(Optional.of(room));
        when(fileStorageService.storeFile(any(MultipartFile.class))).thenReturn("test-image.jpg");
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        ReadRoomDto roomDto = new ReadRoomDto();
        when(roomMapper.toDto(any(Room.class))).thenReturn(roomDto);

        ReadRoomDto result = roomService.uploadRoomPhoto(roomId, file);

        assertNotNull(result);
        verify(fileStorageService, times(1)).storeFile(any(MultipartFile.class));
        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void testUploadRoomPhoto_RoomNotFound() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(roomRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            roomService.uploadRoomPhoto(roomId, file);
        });

        assertEquals("Room not found", exception.getMessage());
        verify(roomRepository, times(1)).findById(roomId);
        verify(roomRepository, never()).save(any(Room.class));
    }

    @Test
    void testGetAvailableRooms_Success() {
        Page<Room> roomPage = new PageImpl<>(List.of(room));
        when(roomRepository.findAllActiveRoomsAndCapacityGreaterThanEqual(anyInt(), any(Pageable.class))).thenReturn(roomPage);
        when(reservationServiceClient.getUnavailableRoomIds(anyList(), any(LocalDate.class), any(LocalDate.class), any(Pageable.class))).thenReturn(Page.empty());

        Page<ReadRoomDto> result = roomService.getAvailableRooms(LocalDate.now(), LocalDate.now().plusDays(3), 2, PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        verify(roomRepository, times(1)).findAllActiveRoomsAndCapacityGreaterThanEqual(anyInt(), any(Pageable.class));
        verify(reservationServiceClient, times(1)).getUnavailableRoomIds(anyList(), any(LocalDate.class), any(LocalDate.class), any(Pageable.class));
    }
}

package com.hotel.room_service.controller;

import com.hotel.room_service.dto.RoomMapper;
import com.hotel.room_service.dto.request.CreateRoomDto;
import com.hotel.room_service.dto.response.ReadRoomDto;
import com.hotel.room_service.entity.Room;
import com.hotel.room_service.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RoomControllerTest {

    @Mock
    private RoomService roomService;

    @Mock
    private RoomMapper roomMapper;

    @InjectMocks
    private RoomController roomController;

    private Room room;
    private ReadRoomDto readRoomDto;
    private CreateRoomDto createRoomDto;
    private UUID roomId;
    private final Path rootLocation = Paths.get("src/main/resources/static/images");
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        roomId = UUID.randomUUID();
        room = new Room();
        room.setId(roomId);
        readRoomDto = new ReadRoomDto();
        createRoomDto = new CreateRoomDto();
    }


    @Test
    void testGetRoomDetails_Found() {
        when(roomService.findById(any(UUID.class))).thenReturn(room);
        when(roomMapper.toDto(room)).thenReturn(readRoomDto);

        ResponseEntity<?> response = roomController.getRoomDetails(roomId);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(roomService, times(1)).findById(roomId);
    }

    @Test
    void testGetRoomDetails_NotFound() {
        when(roomService.findById(any(UUID.class))).thenReturn(null);

        ResponseEntity<?> response = roomController.getRoomDetails(roomId);

        assertEquals("Room not found", response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(roomService, times(1)).findById(roomId);
    }


    @Test
    void testCreateNewRoom() {
        when(roomService.create(any(Room.class))).thenReturn(room);
        when(roomMapper.toEntity(any(CreateRoomDto.class))).thenReturn(room);
        when(roomMapper.toDto(any(Room.class))).thenReturn(readRoomDto);

        ResponseEntity<?> response = roomController.createNewRoom(createRoomDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(roomService, times(1)).create(any(Room.class));
    }

    @Test
    void testActivateRoomStatus() {
        when(roomService.updateStatus(any(UUID.class), eq("active"))).thenReturn(room);
        when(roomMapper.toDto(any(Room.class))).thenReturn(readRoomDto);

        ResponseEntity<?> response = roomController.activateRoomStatus(roomId);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(roomService, times(1)).updateStatus(roomId, "active");
    }

    @Test
    void testDeactivateRoomStatus() {
        when(roomService.updateStatus(any(UUID.class), eq("inactive"))).thenReturn(room);
        when(roomMapper.toDto(any(Room.class))).thenReturn(readRoomDto);

        ResponseEntity<?> response = roomController.deactivateRoomStatus(roomId);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(roomService, times(1)).updateStatus(roomId, "inactive");
    }

    @Test
    void testFindAllSorted() {
        Page<Room> pageRoom = new PageImpl<>(Collections.singletonList(room));
        when(roomService.findAllSorted(anyInt(), anyInt(), anyString(), anyString())).thenReturn(pageRoom);
        when(roomMapper.toListDto(anyList())).thenReturn(Arrays.asList(readRoomDto));

        ResponseEntity<?> response = roomController.findAllSorted(0, 10, "roomType", "asc");

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(roomService, times(1)).findAllSorted(0, 10, "roomType", "asc");
    }

    @Test
    void testFindAllActiveRooms() {
        Page<Room> pageRoom = new PageImpl<>(Collections.singletonList(room));
        when(roomService.findAllActiveSorted(anyInt(), anyInt(), anyString(), anyString())).thenReturn(pageRoom);
        when(roomMapper.toListDto(anyList())).thenReturn(Arrays.asList(readRoomDto));

        ResponseEntity<?> response = roomController.findAllActiveRooms(0, 10, "price", "asc");

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(roomService, times(1)).findAllActiveSorted(0, 10, "price", "asc");
    }

    @Test
    void testFilterRooms() {
        Page<Room> pageRoom = new PageImpl<>(Collections.singletonList(room));
        when(roomService.search(anyInt(), anyInt(), anyString(), anyString(), anyString(), anyInt(), any(BigDecimal.class)))
                .thenReturn(pageRoom);
        when(roomMapper.toListDto(anyList())).thenReturn(Arrays.asList(readRoomDto));

        ResponseEntity<?> response = roomController.filterRooms(0, 10, "active", "wifi", 2, "single", BigDecimal.valueOf(100));

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(roomService, times(1)).search(anyInt(), anyInt(), anyString(), anyString(), anyString(), anyInt(), any(BigDecimal.class));
    }

    @Test
    void testEditRoomDetails() {
        when(roomService.updateRoom(any(UUID.class), any(Room.class))).thenReturn(room);
        when(roomMapper.toEntity(any(CreateRoomDto.class))).thenReturn(room);
        when(roomMapper.toDto(any(Room.class))).thenReturn(readRoomDto);

        ResponseEntity<?> response = roomController.editRoomDetails(createRoomDto, roomId);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(roomService, times(1)).updateRoom(roomId, room);
    }

    @Test
    void testDeleteRoom() {
        doNothing().when(roomService).deleteRoom(any(UUID.class));

        ResponseEntity<?> response = roomController.deleteRoom(roomId);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(roomService, times(1)).deleteRoom(roomId);
    }

    @Test
    void testUploadUserPhoto_Success() throws Exception {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(roomService.uploadRoomPhoto(any(UUID.class), any(MultipartFile.class))).thenReturn(readRoomDto);

        ResponseEntity<?> response = roomController.uploadUserPhoto(roomId, mockFile);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(roomService, times(1)).uploadRoomPhoto(any(UUID.class), any(MultipartFile.class));
    }

    @Test
    void testGetAvailableRooms() {
        // Given
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = checkIn.plusDays(1);
        int capacity = 2;
        Pageable pageable = PageRequest.of(0, 10);

        // Create mock room data
        ReadRoomDto roomDto1 = new ReadRoomDto(/* set fields accordingly */);
        ReadRoomDto roomDto2 = new ReadRoomDto(/* set fields accordingly */);
        List<ReadRoomDto> roomDtoList = Arrays.asList(roomDto1, roomDto2);

        // Create a mock page of ReadRoomDto
        Page<ReadRoomDto> roomPage = new PageImpl<>(roomDtoList, pageable, roomDtoList.size());

        // Mock the roomService response
        when(roomService.getAvailableRooms(checkIn, checkOut, capacity, pageable)).thenReturn(roomPage);

        // When
        Page<ReadRoomDto> response = roomController.getAvailableRooms(checkIn, checkOut, capacity, pageable);

        // Then
        assertEquals(2, response.getTotalElements());
        assertEquals(roomDtoList, response.getContent());

        // Verify that the service method was called
        verify(roomService, times(1)).getAvailableRooms(checkIn, checkOut, capacity, pageable);
    }

    @Test
    void testGetImage_Success() throws Exception {
        // Given
        String filename = "testImage.jpg";
        Path filePath = rootLocation.resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        // Use a spy to mock the behavior of UrlResource
        Resource mockResource = spy(resource);

        // Mock the resource to return true for exists() method
        when(mockResource.exists()).thenReturn(true);

        // When
        ResponseEntity<?> response = roomController.getImage(filename);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.IMAGE_JPEG, response.getHeaders().getContentType());
        assertEquals("inline; filename=\"" + mockResource.getFilename() + "\"", response.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION).get(0));
    }

}

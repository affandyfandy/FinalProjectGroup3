package com.hotel.room_service.controller;

import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import com.hotel.room_service.dto.RoomMapper;
import com.hotel.room_service.dto.RoomMapperImpl;
import com.hotel.room_service.dto.request.CreateRoomDto;
import com.hotel.room_service.dto.response.ReadRoomDto;
import com.hotel.room_service.entity.Room;
import com.hotel.room_service.entity.RoomType;
import com.hotel.room_service.entity.Status;
import com.hotel.room_service.service.RoomService;

public class RoomControllerTest {
    @InjectMocks
    private RoomController roomController;

    @Mock
    private RoomService roomService;

    @Mock
    private RoomMapper roomMapper;

    private Room room;
    private ReadRoomDto roomDto;
    private UUID roomId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        roomId = UUID.randomUUID();
        room = new Room();
        room.setId(roomId);
        room.setRoomNumber("DO9000");
        room.setRoomType(RoomType.SINGLE);
        room.setCapacity(5);
        room.setPrice(BigDecimal.valueOf(100001000));
        room.setStatus(Status.ACTIVE);

        roomDto = new ReadRoomDto();
        roomDto.setId(roomId);
        roomDto.setRoomNumber("DO9000");
        roomDto.setRoomType("SUITE");
        roomDto.setCapacity(5);
        roomDto.setPrice(BigDecimal.valueOf(100001000));
        roomDto.setStatus("ACTIVE");
    }

    @Test
    void testGetRoomDetails() {
        when(roomService.findById(roomId)).thenReturn(room);
        when(roomMapper.toDto(room)).thenReturn(roomDto);

        ResponseEntity<?> response = roomController.getRoomDetails(roomId);

        assertEquals(202, response.getStatusCodeValue());
        assertEquals(roomDto, response.getBody());
        verify(roomService, times(1)).findById(roomId);
        verify(roomMapper, times(1)).toDto(room);
    }

    @Test
    void testCreateNewRoom() {
        CreateRoomDto createRoomDto = new CreateRoomDto();
        createRoomDto.setRoomNumber("DO9000");
        createRoomDto.setRoomType("SUITE");
        createRoomDto.setCapacity(5);
        createRoomDto.setPrice(BigDecimal.valueOf(100001000));

        when(roomMapper.toEntity(createRoomDto)).thenReturn(room);
        when(roomService.create(room)).thenReturn(room);
        when(roomMapper.toDto(room)).thenReturn(roomDto);

        ResponseEntity<?> response = roomController.createNewRoom(createRoomDto);

        assertEquals(202, response.getStatusCodeValue());
        assertEquals(roomDto, response.getBody());
        verify(roomMapper, times(1)).toEntity(createRoomDto);
        verify(roomService, times(1)).create(room);
        verify(roomMapper, times(1)).toDto(room);
    }

    @Test
    void testActivateRoomStatus() {
        when(roomService.updateStatus(roomId, "active")).thenReturn(room);
        when(roomMapper.toDto(room)).thenReturn(roomDto);

        ResponseEntity<?> response = roomController.activateRoomStatus(roomId);

        assertEquals(202, response.getStatusCodeValue());
        assertEquals(roomDto, response.getBody());
        verify(roomService, times(1)).updateStatus(roomId, "active");
        verify(roomMapper, times(1)).toDto(room);
    }

    @Test
    void testDeactivateRoomStatus() {
        when(roomService.updateStatus(roomId, "inactive")).thenReturn(room);
        when(roomMapper.toDto(room)).thenReturn(roomDto);

        ResponseEntity<?> response = roomController.deactivateRoomStatus(roomId);

        assertEquals(202, response.getStatusCodeValue());
        assertEquals(roomDto, response.getBody());
        verify(roomService, times(1)).updateStatus(roomId, "inactive");
        verify(roomMapper, times(1)).toDto(room);
    }

    @Test
    void testFindAllSorted() {
        List<Room> roomList = new ArrayList<>();
        roomList.add(room);
        Page<Room> roomPage = new PageImpl<>(roomList);

        when(roomService.findAllSorted(0, 10, "roomType", "asc")).thenReturn(roomPage);
        when(roomMapper.toListDto(roomList)).thenReturn(List.of(roomDto));

        ResponseEntity<?> response = roomController.findAllSorted(0, 10, "roomType", "asc");

        assertEquals(202, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(roomService, times(1)).findAllSorted(0, 10, "roomType", "asc");
        verify(roomMapper, times(1)).toListDto(roomList);
    }

    @Test
    void testFilterRooms() {
        List<Room> roomList = new ArrayList<>();
        roomList.add(room);
        Page<Room> roomPage = new PageImpl<>(roomList);

        when(roomService.search(0, 10, null, null, null, null, null, null)).thenReturn(roomPage);
        when(roomMapper.toListDto(roomList)).thenReturn(List.of(roomDto));

        ResponseEntity<?> response = roomController.filterRooms(0, 10, null, null, null, null, null, null);

        assertEquals(202, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(roomService, times(1)).search(0, 10, null, null, null, null, null, null);
        verify(roomMapper, times(1)).toListDto(roomList);
    }

    @Test
    void testEditRoomDetails() {
        CreateRoomDto createRoomDto = new CreateRoomDto();
        createRoomDto.setRoomNumber("DO9000");
        createRoomDto.setRoomType("SUITE");
        createRoomDto.setCapacity(5);
        createRoomDto.setPrice(BigDecimal.valueOf(100001000));

        when(roomService.updateRoom(roomId, roomMapper.toEntity(createRoomDto))).thenReturn(room);
        when(roomMapper.toEntity(createRoomDto)).thenReturn(room);
        when(roomMapper.toDto(room)).thenReturn(roomDto);

        ResponseEntity<?> response = roomController.editRoomDetails(createRoomDto, roomId);

        assertEquals(202, response.getStatusCodeValue());
        assertEquals(roomDto, response.getBody());
        verify(roomService, times(1)).updateRoom(roomId, roomMapper.toEntity(createRoomDto));
        verify(roomMapper, times(1)).toEntity(createRoomDto);
        verify(roomMapper, times(1)).toDto(room);
    }
}
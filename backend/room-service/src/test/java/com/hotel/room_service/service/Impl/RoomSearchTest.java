package com.hotel.room_service.service.Impl;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.hotel.room_service.entity.Room;
import com.hotel.room_service.repository.RoomRepository;
import com.hotel.room_service.service.RoomServiceImpl;

public class RoomSearchTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomServiceImpl roomService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSearchStatusOnly() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "status"));
        Page<Room> roomPage = new PageImpl<>(Collections.emptyList());

        when(roomRepository.findAllByStatus(anyString(), eq(pageable)))
                .thenReturn(roomPage);

        Page<Room> result = roomService.search(0, 10, "active", null, null, null, null);
        
        assertEquals(roomPage, result);
        verify(roomRepository, times(1)).findAllByStatus(anyString(), eq(pageable));
    }

    @Test
    public void testSearchStatusAndFacility() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "status"));
        Page<Room> roomPage = new PageImpl<>(Collections.emptyList());

        when(roomRepository.findAllByStatusAndFacility(anyString(), anyString(), eq(pageable)))
                .thenReturn(roomPage);

        Page<Room> result = roomService.search(0, 10, "active", "TV", null, null, null);
        
        assertEquals(roomPage, result);
        verify(roomRepository, times(1)).findAllByStatusAndFacility(anyString(), anyString(), eq(pageable));
    }

    @Test
    public void testSearchStatusAndRoomType() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "status"));
        Page<Room> roomPage = new PageImpl<>(Collections.emptyList());

        when(roomRepository.findAllByStatusAndRoomType(anyString(), anyString(), eq(pageable)))
                .thenReturn(roomPage);

        Page<Room> result = roomService.search(0, 10, "active", null, "SINGLE", null, null);
        
        assertEquals(roomPage, result);
        verify(roomRepository, times(1)).findAllByStatusAndRoomType(anyString(), anyString(), eq(pageable));
    }

    @Test
    public void testSearchStatusAndCapacity() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "status"));
        Page<Room> roomPage = new PageImpl<>(Collections.emptyList());

        when(roomRepository.findAllByStatusAndCapacity(anyString(), anyInt(), eq(pageable)))
                .thenReturn(roomPage);

        Page<Room> result = roomService.search(0, 10, "active", null, null, 2, null);
        
        assertEquals(roomPage, result);
        verify(roomRepository, times(1)).findAllByStatusAndCapacity(anyString(), anyInt(), eq(pageable));
    }

    @Test
    public void testSearchStatusAndPriceRange() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "status"));
        Page<Room> roomPage = new PageImpl<>(Collections.emptyList());

        when(roomRepository.findAllByStatusAndPriceRange(anyString(), any(BigDecimal.class), eq(pageable)))
                .thenReturn(roomPage);

        Page<Room> result = roomService.search(0, 10, "active", null, null, null, BigDecimal.valueOf(100.00));
        
        assertEquals(roomPage, result);
        verify(roomRepository, times(1)).findAllByStatusAndPriceRange(anyString(), any(BigDecimal.class), eq(pageable));
    }

    @Test
    public void testSearchStatusFacilityAndRoomType() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "status"));
        Page<Room> roomPage = new PageImpl<>(Collections.emptyList());

        when(roomRepository.findAllByStatusAndFacilityAndRoomType(anyString(), anyString(), anyString(), eq(pageable)))
                .thenReturn(roomPage);

        Page<Room> result = roomService.search(0, 10, "active", "TV", "SINGLE", null, null);
        
        assertEquals(roomPage, result);
        verify(roomRepository, times(1)).findAllByStatusAndFacilityAndRoomType(anyString(), anyString(), anyString(), eq(pageable));
    }

    @Test
    public void testSearchStatusFacilityAndCapacity() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "status"));
        Page<Room> roomPage = new PageImpl<>(Collections.emptyList());

        when(roomRepository.findAllByStatusAndFacilityAndCapacity(anyString(), anyString(), anyInt(), eq(pageable)))
                .thenReturn(roomPage);

        Page<Room> result = roomService.search(0, 10, "active", "TV", null, 2, null);
        
        assertEquals(roomPage, result);
        verify(roomRepository, times(1)).findAllByStatusAndFacilityAndCapacity(anyString(), anyString(), anyInt(), eq(pageable));
    }

    @Test
    public void testSearchNoFilters() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "status"));
        Page<Room> roomPage = new PageImpl<>(Collections.emptyList());

        when(roomRepository.findAll(eq(pageable)))
                .thenReturn(roomPage);

        Page<Room> result = roomService.search(0, 10, null, null, null, null, null);
        
        assertEquals(roomPage, result);
        verify(roomRepository, times(1)).findAll(eq(pageable));
    }
}

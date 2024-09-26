package com.hotel.room_service.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    private Room room;

    @BeforeEach
    void setUp() {
        room = new Room();
        room.setId(UUID.randomUUID());
        room.setRoomNumber("101");
        room.setCapacity(2);
        room.setStatus(Status.ACTIVE);
        room.setRoomType(RoomType.SINGLE);
        room.setPrice(BigDecimal.valueOf(100.0));
        List<Facility> facilities = new ArrayList<>();
        facilities.add(Facility.WIFI);
        facilities.add(Facility.TELEVISION);
        room.setFacility(facilities);
    }

    @Test
    void testGetId() {
        assertNotNull(room.getId());
    }

    @Test
    void testGetRoomNumber() {
        assertEquals("101", room.getRoomNumber());
    }

    @Test
    void testGetCapacity() {
        assertEquals(2, room.getCapacity());
    }

    @Test
    void testGetStatus() {
        assertEquals(Status.ACTIVE, room.getStatus());
    }

    @Test
    void testGetRoomType() {
        assertEquals(RoomType.SINGLE, room.getRoomType());
    }

    @Test
    void testGetPrice() {
        assertEquals(BigDecimal.valueOf(100.0), room.getPrice());
    }

    @Test
    void testGetFacilities() {
        assertEquals(2, room.getFacility().size());
        assertTrue(room.getFacility().contains(Facility.WIFI));
        assertTrue(room.getFacility().contains(Facility.TELEVISION));
    }
}

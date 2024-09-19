package com.hotel.room_service.controller;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.room_service.entity.Room;
import com.hotel.room_service.entity.RoomType;
import com.hotel.room_service.service.RoomServiceImpl;

@WebMvcTest(controllers = RoomController.class)
public class RoomControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoomServiceImpl roomService;

    private Room room;

    @BeforeEach
    private void setup(){
        room = new Room();
        room.setId(UUID.randomUUID());
        room.setCapacity(5);
        room.setFacility("View");
        // room.setPrice(10000.0);
        room.setRoomNumber("AD990");
        room.setRoomType(RoomType.SINGLE);
    }

    @AfterEach
    private void cleanup(){
        room = null;
    }

    @Test
    @DisplayName("Test 1: Get room by room Id")
    public void testGetExistingByRoomId() throws Exception{
        Mockito.when(roomService.findById(Mockito.any(UUID.class))).thenReturn(room);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/room/"+room.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    
}

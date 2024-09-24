package com.hotel.auth_service.controller;

import com.hotel.auth_service.dto.UserDto;
import com.hotel.auth_service.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @MockBean
    private UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService)).build();
    }

    @Test
    void testGetAllUsers() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setFullName("John Doe");
        userDto.setRole("USER");
        userDto.setStatus("ACTIVE");
        userDto.setPhone("1234567890");

        Page<UserDto> userDtos = new PageImpl<>(Collections.singletonList(userDto), pageable, 1);

        when(userService.getAllUsers(pageable)).thenReturn(userDtos);

        mockMvc.perform(get("/api/v1/users?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].email").value("test@example.com"))
                .andExpect(jsonPath("$.content[0].fullName").value("John Doe"));

        verify(userService, times(1)).getAllUsers(pageable);
    }

    @Test
    void testGetUserById_UserFound() throws Exception {
        String email = "test@example.com";

        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        userDto.setFullName("John Doe");
        userDto.setRole("USER");
        userDto.setStatus("ACTIVE");
        userDto.setPhone("1234567890");

        when(userService.getUserByEmail(email)).thenReturn(Optional.of(userDto));

        mockMvc.perform(get("/api/v1/users/{id}", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.fullName").value("John Doe"));

        verify(userService, times(1)).getUserByEmail(email);
    }

    @Test
    void testCreateUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setFullName("John Doe");
        userDto.setPassword("password123");
        userDto.setRole("USER");

        UserDto createdUser = new UserDto();
        createdUser.setEmail("test@example.com");
        createdUser.setFullName("John Doe");
        createdUser.setRole("USER");
        createdUser.setStatus("ACTIVE");

        when(userService.createUser(any(UserDto.class))).thenReturn(createdUser);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.fullName").value("John Doe"));

        verify(userService, times(1)).createUser(any(UserDto.class));
    }

    @Test
    void testUpdateUser_UserFound() throws Exception {
        String email = "test@example.com";
        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        userDto.setFullName("John Doe");
        userDto.setRole("USER");

        UserDto updatedUser = new UserDto();
        updatedUser.setEmail(email);
        updatedUser.setFullName("John Doe");
        updatedUser.setRole("USER");

        when(userService.updateUser(eq(email), any(UserDto.class))).thenReturn(Optional.of(updatedUser));

        mockMvc.perform(put("/api/v1/users/{id}", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.fullName").value("John Doe"));

        verify(userService, times(1)).updateUser(eq(email), any(UserDto.class));
    }

    @Test
    void testChangeUserPassword_UserFound() throws Exception {
        String email = "test@example.com";
        String password = "newPassword";
        UserDto updatedUser = new UserDto();
        updatedUser.setEmail(email);
        updatedUser.setFullName("John Doe");

        when(userService.changeUserPassword(eq(email), anyString())).thenReturn(Optional.of(updatedUser));

        mockMvc.perform(put("/api/v1/users/{id}/password", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(password))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.fullName").value("John Doe"));

        verify(userService, times(1)).changeUserPassword(eq(email), eq(password));
    }

    @Test
    void testToggleUserStatus_UserFound() throws Exception {
        String email = "test@example.com";
        UserDto updatedUser = new UserDto();
        updatedUser.setEmail(email);
        updatedUser.setStatus("INACTIVE");

        when(userService.toggleUserStatus(email)).thenReturn(Optional.of(updatedUser));

        mockMvc.perform(put("/api/v1/users/{id}/status", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.status").value("INACTIVE"));

        verify(userService, times(1)).toggleUserStatus(email);
    }

    @Test
    void testDeleteUser() throws Exception {
        String email = "test@example.com";

        doNothing().when(userService).deleteUser(email);

        mockMvc.perform(delete("/api/v1/users/{id}", email))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(email);
    }
}

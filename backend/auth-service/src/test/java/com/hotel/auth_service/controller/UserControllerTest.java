package com.hotel.auth_service.controller;

import com.hotel.auth_service.criteria.UserSearchCriteria;
import com.hotel.auth_service.dto.UserDto;
import com.hotel.auth_service.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsers() {
        // Arrange
        Page<UserDto> page = new PageImpl<>(List.of(new UserDto()));
        when(userService.getAllUsers(any(PageRequest.class), any(UserSearchCriteria.class))).thenReturn(page);

        // Act
        ResponseEntity<Page<UserDto>> response = userController.getAllUsers(PageRequest.of(0, 10), new UserSearchCriteria());

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    public void testGetUserById_Success() {
        // Arrange
        UserDto userDto = new UserDto();
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.of(userDto));

        // Act
        ResponseEntity<UserDto> response = userController.getUserById("test-id");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userDto, response.getBody());
    }

    @Test
    public void testGetUserById_NotFound() {
        // Arrange
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        try {
            userController.getUserById("test-id");
        } catch (IllegalArgumentException e) {
            assertEquals("User not found", e.getMessage());
        }
    }

    @Test
    public void testCreateUser() {
        // Arrange
        UserDto userDto = new UserDto();
        when(userService.createUser(any(UserDto.class))).thenReturn(userDto);

        // Act
        ResponseEntity<UserDto> response = userController.createUser(userDto);

        // Assert
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(userDto, response.getBody());
    }

    @Test
    public void testUploadUserPhoto_Success() throws Exception {
        // Arrange
        MultipartFile file = mock(MultipartFile.class);
        UserDto userDto = new UserDto();
        when(userService.uploadUserPhoto(anyString(), any(MultipartFile.class))).thenReturn(userDto);

        // Act
        ResponseEntity<?> response = userController.uploadUserPhoto("test-id", file);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
    }

}

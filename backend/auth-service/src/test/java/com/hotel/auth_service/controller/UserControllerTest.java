package com.hotel.auth_service.controller;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import com.hotel.auth_service.dto.UserDto;
import com.hotel.auth_service.service.UserService;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void getUserById_Success() {
        String userId = "123";
        UserDto userDto = new UserDto();
        when(userService.getUserByEmail(userId)).thenReturn(Optional.of(userDto));

        ResponseEntity<UserDto> response = userController.getUserById(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userDto, response.getBody());
        verify(userService).getUserByEmail(userId);
    }

    @Test
    void createUser_Success() {
        UserDto userDto = new UserDto();
        when(userService.createUser(any(UserDto.class))).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.createUser(userDto);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(userDto, response.getBody());
        verify(userService).createUser(any(UserDto.class));
    }

    @Test
    void updateUser_Success() {
        String userId = "123";
        UserDto userDto = new UserDto();
        when(userService.updateUser(userId, userDto)).thenReturn(Optional.of(userDto));

        ResponseEntity<UserDto> response = userController.updateUser(userId, userDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userDto, response.getBody());
        verify(userService).updateUser(userId, userDto);
    }

    @Test
    void deleteUser_Success() {
        String userId = "123";

        ResponseEntity<Void> response = userController.deleteUser(userId);

        assertEquals(204, response.getStatusCodeValue());
        verify(userService).deleteUser(userId);
    }

    @Test
    void uploadUserPhoto_Success() throws Exception {
        String userId = "123";
        MultipartFile file = mock(MultipartFile.class);
        UserDto userDto = new UserDto();
        when(userService.uploadUserPhoto(userId, file)).thenReturn(userDto);

        ResponseEntity<?> response = userController.uploadUserPhoto(userId, file);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userDto, response.getBody());
        verify(userService).uploadUserPhoto(userId, file);
    }
}

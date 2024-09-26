package com.hotel.auth_service.controller;

import com.hotel.auth_service.dto.AuthDto;
import com.hotel.auth_service.dto.RequestNewPasswordDto;
import com.hotel.auth_service.dto.UserDto;
import com.hotel.auth_service.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogin_Success() throws IllegalAccessException {
        // Arrange
        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest("testuser", "password");
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getAuthorities()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        // Sesuaikan dengan nama field token di AuthDto.Response
        AuthDto.Response responseDto = new AuthDto.Response("User logged in successfully", "mockToken");
        when(authService.generateToken(authentication)).thenReturn("mockToken");

        // Act
        ResponseEntity<?> response = authController.login(loginRequest);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseDto.getMessage(), ((AuthDto.Response) response.getBody()).getMessage());
        assertEquals(responseDto.getToken(), ((AuthDto.Response) response.getBody()).getToken());
    }


    @Test
    public void testLogin_InvalidCredentials() {
        // Arrange
        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest("invaliduser", "wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new IllegalArgumentException("Invalid credentials"));

        // Act & Assert
        try {
            authController.login(loginRequest);
        } catch (IllegalAccessException e) {
            assertEquals("Invalid credentials", e.getMessage());
        }
    }

    @Test
    public void testRegister_Success() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmail("testuser@example.com");
        userDto.setPassword("password");
        userDto.setRole("ROLE_USER");

        when(authService.registerUser(any(UserDto.class))).thenReturn(userDto);
        when(authService.generateToken(any(Authentication.class))).thenReturn("mockToken");

        // Act
        ResponseEntity<?> response = authController.register(userDto);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User logged in successfully", ((AuthDto.Response) response.getBody()).getMessage());
        assertEquals("mockToken", ((AuthDto.Response) response.getBody()).getToken());
    }

    @Test
    public void testResetPassword_Success() {
        // Arrange
        RequestNewPasswordDto requestNewPasswordDto = new RequestNewPasswordDto();
        doNothing().when(authService).forgotPassword(any(RequestNewPasswordDto.class));

        // Act
        ResponseEntity<?> response = authController.resetPassword(requestNewPasswordDto);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Password changed successfully", ((Map<String, String>) response.getBody()).get("message"));
    }
}

package com.hotel.auth_service.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.hotel.auth_service.dto.AuthDto;
import com.hotel.auth_service.dto.RequestNewPasswordDto;
import com.hotel.auth_service.service.AuthService;

public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_Success() throws IllegalAccessException {
        // Arrange
        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest("test@example.com", "password");
        String token = "generatedToken";
        
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authService.generateToken(authentication)).thenReturn(token);
        
        // Act
        ResponseEntity<?> response = authController.login(loginRequest);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        AuthDto.Response body = (AuthDto.Response) response.getBody();
        assertEquals("User logged in successfully", body.message());
        assertEquals(token, body.token());
        
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(authService).generateToken(authentication);
    }

    @Test
    void resetPassword_Success() {
        // Arrange
        RequestNewPasswordDto requestNewPasswordDto = new RequestNewPasswordDto();
        requestNewPasswordDto.setEmail("test@example.com");
        requestNewPasswordDto.setNewPassword("newPassword");

        // Act
        ResponseEntity<?> response = authController.resetPassword(requestNewPasswordDto);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Password changed successfully", ((java.util.Map<?, ?>) response.getBody()).get("message"));

        verify(authService).forgotPassword(requestNewPasswordDto);
    }
}

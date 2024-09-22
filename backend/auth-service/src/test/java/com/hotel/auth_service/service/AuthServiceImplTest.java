package com.hotel.auth_service.service;

import com.hotel.auth_service.dto.RequestNewPasswordDto;
import com.hotel.auth_service.dto.UserDto;
import com.hotel.auth_service.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {
    @Mock
    private JwtEncoder jwtEncoder;

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateToken() {
        when(authentication.getName()).thenReturn("testuser");

        GrantedAuthority authority = new SimpleGrantedAuthority("CUSTOMER");
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(authority);
        when(authentication.getAuthorities()).thenReturn((Collection) authorities);

        Jwt jwt = mock(Jwt.class);
        when(jwt.getTokenValue()).thenReturn("mocked-jwt-token");

        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);

        String token = authService.generateToken(authentication);

        assertNotNull(token);
        assertEquals("mocked-jwt-token", token);

        verify(jwtEncoder, times(1)).encode(any(JwtEncoderParameters.class));
    }

    @Test
    void testRegisterUser() {
        UserDto userDto = new UserDto(
                "testuser@example.com",
                "hashedPassword",
                "CUSTOMER",
                "Test User",
                "+62123456789",
                LocalDate.of(1990, 1, 1),
                "photoUrl",
                "Test Address",
                "ACTIVE",
                "admin",
                "admin",
                LocalDate.now(),
                LocalDate.now());

        // Mock userService createUser method behavior
        when(userService.createUser(any(UserDto.class))).thenReturn(userDto);

        // Test the registerUser method
        UserDto result = authService.registerUser(userDto);

        assertNotNull(result);
        assertEquals(userDto, result);

        verify(userService, times(1)).createUser(userDto);
    }

    @Test
    void testForgotPassword_UserFound() {
        RequestNewPasswordDto requestNewPasswordDto = new RequestNewPasswordDto("testuser@example.com", "newPassword");
        UserDto userDto = new UserDto(
                "testuser@example.com",
                "newPassword",
                "CUSTOMER",
                "Test User",
                "+62123456789",
                LocalDate.of(1990, 1, 1),
                "photoUrl",
                "Test Address",
                "ACTIVE",
                "admin",
                "admin",
                LocalDate.now(),
                LocalDate.now());

        when(userService.changeUserPassword(anyString(), anyString())).thenReturn(Optional.of(userDto));

        UserDto result = authService.forgotPassword(requestNewPasswordDto);

        assertNotNull(result);
        assertEquals(userDto, result);

        verify(userService, times(1)).changeUserPassword(requestNewPasswordDto.getEmail(), requestNewPasswordDto.getNewPassword());
    }

    @Test
    void testForgotPassword_UserNotFound() {
        RequestNewPasswordDto requestNewPasswordDto = new RequestNewPasswordDto("unknown@example.com", "newPassword");

        when(userService.changeUserPassword(anyString(), anyString())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> authService.forgotPassword(requestNewPasswordDto));

        verify(userService, times(1)).changeUserPassword(requestNewPasswordDto.getEmail(), requestNewPasswordDto.getNewPassword());
    }
}

package com.hotel.auth_service.controller;

import com.hotel.auth_service.dto.RequestNewPasswordDto;
import com.hotel.auth_service.dto.UserDto;
import com.hotel.auth_service.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collection;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class AuthControllerTest {
    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void testLogin() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(authentication.getName()).thenReturn("testuser");

        Collection<SimpleGrantedAuthority> grantedAuthorities = Collections.singletonList(
                new SimpleGrantedAuthority("CUSTOMER")
        );

        doReturn(grantedAuthorities).when(authentication).getAuthorities();

        when(authService.generateToken(authentication)).thenReturn("mocked-jwt-token");

        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"User logged in successfully\",\"token\":\"mocked-jwt-token\"}"))
                .andDo(print());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(authService, times(1)).generateToken(any(Authentication.class));
    }

    @Test
    void testRegister() throws Exception {
        UserDto userDto = new UserDto("testuser@example.com", "password", "ROLE_USER",
                "Test User", "+62123456789", null, null, null, "ACTIVE", null, null, null, null);

        when(authService.registerUser(any(UserDto.class))).thenReturn(userDto);

        Authentication authentication = mock(Authentication.class);

        Collection<SimpleGrantedAuthority> grantedAuthorities = Collections.singletonList(
                new SimpleGrantedAuthority("ADMIN")
        );
        doReturn(grantedAuthorities).when(authentication).getAuthorities();

        when(authService.generateToken(any(Authentication.class))).thenReturn("mocked-jwt-token");

        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"testuser@example.com\",\"password\":\"password\",\"role\":\"ROLE_USER\",\"fullName\":\"Test User\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"User logged in successfully\",\"token\":\"mocked-jwt-token\"}"))
                .andDo(print());

        verify(authService, times(1)).registerUser(any(UserDto.class));
        verify(authService, times(1)).generateToken(any(Authentication.class));
    }

    @Test
    void testForgotPassword() throws Exception {
        UserDto userDto = new UserDto("testuser@example.com", "newPassword", "ROLE_USER",
                "Test User", "+62123456789", null, null, null, "ACTIVE", null, null, null, null);

        when(authService.forgotPassword(any(RequestNewPasswordDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/api/v1/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"testuser@example.com\",\"newPassword\":\"newPassword\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"Password changed successfully\"}"))
                .andDo(print());

        verify(authService, times(1)).forgotPassword(any(RequestNewPasswordDto.class));
    }
}

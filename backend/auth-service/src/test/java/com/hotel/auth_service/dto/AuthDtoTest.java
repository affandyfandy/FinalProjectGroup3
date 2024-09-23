package com.hotel.auth_service.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthDtoTest {

    @Test
    void testAuthDtoCreation() {
        AuthDto authDto = new AuthDto();
        assertNotNull(authDto);
    }

    @Test
    void testLoginRequestCreation() {
        String username = "alif@example.com";
        String password = "123456";

        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest(username, password);

        assertNotNull(loginRequest);
        assertEquals(username, loginRequest.username());
        assertEquals(password, loginRequest.password());
    }

    @Test
    void testResponseCreation() {
        String message = "User logged in successfully";
        String token = "jwtToken";

        AuthDto.Response response = new AuthDto.Response(message, token);

        assertNotNull(response);
        assertEquals(message, response.message());
        assertEquals(token, response.token());
    }
}

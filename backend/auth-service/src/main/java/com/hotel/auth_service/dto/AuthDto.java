package com.hotel.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginRequest {
        public String username;
        private String password;
    }

    @Data
    @AllArgsConstructor
    public static class Response {
        public String message;
        public String token;
    }
}

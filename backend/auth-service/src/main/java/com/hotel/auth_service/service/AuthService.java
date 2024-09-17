package com.hotel.auth_service.service;

import org.springframework.security.core.Authentication;

public interface AuthService {
    public String generateToken(Authentication authentication);
}

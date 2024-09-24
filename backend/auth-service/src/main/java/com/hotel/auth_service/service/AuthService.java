package com.hotel.auth_service.service;

import com.hotel.auth_service.dto.RequestNewPasswordDto;
import com.hotel.auth_service.dto.UserDto;
import org.springframework.security.core.Authentication;

public interface AuthService {
    public String generateToken(Authentication authentication);
    public UserDto registerUser(UserDto userDto);
    public UserDto forgotPassword(RequestNewPasswordDto requestNewPasswordDto);
}

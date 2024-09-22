package com.hotel.auth_service.service.impl;

import com.hotel.auth_service.dto.RequestNewPasswordDto;
import com.hotel.auth_service.dto.UserDto;
import com.hotel.auth_service.service.AuthService;
import com.hotel.auth_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtEncoder jwtEncoder;
    private final UserService userService;

    @Autowired
    public AuthServiceImpl(JwtEncoder jwtEncoder, UserService userService) {
        this.jwtEncoder = jwtEncoder;
        this.userService = userService;
    }

    @Override
    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();

        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(10, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("role", role)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Override
    public UserDto registerUser(UserDto userDto) {
        return userService.createUser(userDto);
    }

    @Override
    public UserDto forgotPassword(RequestNewPasswordDto requestNewPasswordDto) {
        Optional<UserDto> user = userService.changeUserPassword(requestNewPasswordDto.getEmail(), requestNewPasswordDto.getNewPassword());

        return user.orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}

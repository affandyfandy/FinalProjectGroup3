package com.hotel.auth_service.controller;

import com.hotel.auth_service.dto.AuthDto;
import com.hotel.auth_service.dto.UserDto;
import com.hotel.auth_service.entity.AuthUser;
import com.hotel.auth_service.service.impl.AuthServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthServiceImpl authService;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(AuthServiceImpl authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDto.LoginRequest userLogin) throws IllegalAccessException {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        userLogin.username(),
                        userLogin.password()
                ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("Token request for user: {}", authentication.getAuthorities());
        String token = authService.generateToken(authentication);
        AuthDto.Response response = new AuthDto.Response("User logged in successfully", token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto) {
        log.info("User registered successfully: {}", userDto);
        UserDto registeredUser = authService.registerUser(userDto);

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(registeredUser.getRole()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                registeredUser.getEmail(),
                registeredUser.getPassword(),
                authorities  // Set the authorities (roles) here
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("Token request for registered user: {}", authentication.getAuthorities());
        String token = authService.generateToken(authentication);
        AuthDto.Response response = new AuthDto.Response("User logged in successfully", token);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/forgot-password")
    public ResponseEntity<?> forgotPassword(@PathVariable("id") String id, @RequestBody String password) {
        authService.forgotPassword(id, password);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Password changed successfully");
        return ResponseEntity.ok(response);
    }
}

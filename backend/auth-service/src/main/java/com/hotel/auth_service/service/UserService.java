package com.hotel.auth_service.service;

import com.hotel.auth_service.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    Page<UserDto> getAllUsers(Pageable pageable);
    Optional<UserDto> getUserByEmail(String email);
    UserDto createUser(UserDto userDto);
    Optional<UserDto> updateUser(String email, UserDto userDto);
    Optional<UserDto> changeUserPassword(String email, String password);
    Optional<UserDto> toggleUserStatus(String email);
    void deleteUser(String email);
}

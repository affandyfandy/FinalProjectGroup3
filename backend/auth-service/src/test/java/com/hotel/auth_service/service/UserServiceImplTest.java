package com.hotel.auth_service.service;

import com.hotel.auth_service.dto.UserDto;
import com.hotel.auth_service.entity.Status;
import com.hotel.auth_service.entity.User;
import com.hotel.auth_service.mapper.UserMapper;
import com.hotel.auth_service.repository.UserRepository;
import com.hotel.auth_service.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_ReturnsNull() {
        String username = "testuser";

        UserDetails result = userService.loadUserByUsername(username);

        assertNull(result, "Expected loadUserByUsername to return null.");
    }

//    @Test
//    void testGetAllUsers() {
//        Pageable pageable = PageRequest.of(0, 10);
//        User user = new User();
//        UserDto userDto = new UserDto();
//        Page<User> users = new PageImpl<>(Collections.singletonList(user));
//
//        when(userRepository.findAll(pageable)).thenReturn(users);
//        when(userMapper.toUserDto(user)).thenReturn(userDto);
//
//        Page<UserDto> result = userService.getAllUsers(pageable);
//        assertNotNull(result);
//        assertEquals(1, result.getTotalElements());
//        assertEquals(userDto, result.getContent().get(0));
//
//        verify(userRepository, times(1)).findAll(pageable);
//        verify(userMapper, times(1)).toUserDto(user);
//    }

    @Test
    void testGetUserByEmail_UserFound() {
        String email = "test@example.com";
        User user = new User();
        UserDto userDto = new UserDto();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        Optional<UserDto> result = userService.getUserByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(userDto, result.get());

        verify(userRepository, times(1)).findByEmail(email);
        verify(userMapper, times(1)).toUserDto(user);
    }

    @Test
    void testGetUserByEmail_UserNotFound() {
        String email = "test@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<UserDto> result = userService.getUserByEmail(email);

        assertFalse(result.isPresent());

        verify(userRepository, times(1)).findByEmail(email);
        verify(userMapper, never()).toUserDto(any(User.class));
    }

    @Test
    void testCreateUser() {
        UserDto userDto = new UserDto();
        userDto.setPassword("plainPassword");
        User user = new User();

        when(userMapper.toUserEntity(userDto)).thenReturn(user);
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        UserDto result = userService.createUser(userDto);

        assertNotNull(result);
        assertEquals(userDto, result);
        assertEquals("encodedPassword", user.getPassword());

        verify(userMapper, times(1)).toUserEntity(userDto);
        verify(passwordEncoder, times(1)).encode("plainPassword");
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toUserDto(user);
    }

    @Test
    void testUpdateUser_UserNotFound() {
        String email = "test@example.com";
        UserDto userDto = new UserDto();

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<UserDto> result = userService.updateUser(email, userDto);

        assertFalse(result.isPresent());

        verify(userRepository, times(1)).findByEmail(email);
        verify(userMapper, never()).toUserEntity(userDto);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testDeleteUser() {
        String email = "test@example.com";

        doNothing().when(userRepository).deleteById(email);

        userService.deleteUser(email);

        verify(userRepository, times(1)).deleteById(email);
    }

    @Test
    void testChangeUserPassword_UserExists() {
        String email = "user@example.com";
        String newPassword = "newPassword123";
        User existingUser = new User();
        existingUser.setEmail(email);
        existingUser.setPassword("oldPassword");

        User savedUser = new User();
        savedUser.setEmail(email);
        savedUser.setPassword("encodedNewPassword");

        UserDto userDto = new UserDto();
        userDto.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");
        when(userRepository.save(existingUser)).thenReturn(savedUser);
        when(userMapper.toUserDto(savedUser)).thenReturn(userDto);

        Optional<UserDto> result = userService.changeUserPassword(email, newPassword);

        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
        verify(userRepository).findByEmail(email);
        verify(passwordEncoder).encode(newPassword);
        verify(userRepository).save(existingUser);
        verify(userMapper).toUserDto(savedUser);
    }


    @Test
    void testChangeUserPassword_UserNotFound() {
        // Given
        String email = "nonexistent@example.com";
        String newPassword = "newPassword123";

        // Mocking
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When
        Optional<UserDto> result = userService.changeUserPassword(email, newPassword);

        // Then
        assertFalse(result.isPresent());
        verify(userRepository).findByEmail(email);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any());
        verify(userMapper, never()).toUserDto(any());
    }

    @Test
    void testToggleUserStatus_UserExists() {
        // Given
        String email = "user@example.com";
        User existingUser = new User();
        existingUser.setEmail(email);
        existingUser.setStatus(Status.ACTIVE);

        User savedUser = new User();
        savedUser.setEmail(email);
        savedUser.setStatus(Status.INACTIVE);

        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        userDto.setStatus(Status.INACTIVE.name());

        // Mocking
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(savedUser);
        when(userMapper.toUserDto(savedUser)).thenReturn(userDto);

        // When
        Optional<UserDto> result = userService.toggleUserStatus(email);

        // Then
        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
        assertEquals(Status.INACTIVE.name(), result.get().getStatus());
        verify(userRepository).findByEmail(email);
        verify(userRepository).save(existingUser);
        verify(userMapper).toUserDto(savedUser);
    }

    @Test
    void testToggleUserStatus_UserNotFound() {
        // Given
        String email = "nonexistent@example.com";

        // Mocking
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When
        Optional<UserDto> result = userService.toggleUserStatus(email);

        // Then
        assertFalse(result.isPresent());
        verify(userRepository).findByEmail(email);
        verify(userRepository, never()).save(any());
        verify(userMapper, never()).toUserDto(any());
    }
}

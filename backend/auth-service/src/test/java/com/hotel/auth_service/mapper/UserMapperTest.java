package com.hotel.auth_service.mapper;

import com.hotel.auth_service.dto.UserDto;
import com.hotel.auth_service.entity.Role;
import com.hotel.auth_service.entity.Status;
import com.hotel.auth_service.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperImplTest {

    private UserMapperImpl userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapperImpl();
    }

    @Test
    void toUserDto_WithNullUser_ReturnsNull() {
        // Act
        UserDto result = userMapper.toUserDto(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toUserDto_WithUser_ReturnsUserDto() {
        // Arrange
        User user = new User();
        user.setAddress("123 Main St");
        user.setCreatedBy("admin");
        user.setCreatedDate(LocalDateTime.now());
        user.setDateOfBirth(LocalDate.of(1990, 1, 1));
        user.setEmail("test@example.com");
        user.setFullName("Test User");
        user.setLastModifiedBy("admin");
        user.setLastModifiedDate(LocalDateTime.now());
        user.setPassword("password");
        user.setPhone("123456789");
        user.setPhoto("photo.jpg");
        user.setRole(Role.CUSTOMER);
        user.setStatus(Status.ACTIVE);

        // Act
        UserDto result = userMapper.toUserDto(user);

        // Assert
        assertNotNull(result);
        assertEquals(user.getAddress(), result.getAddress());
        assertEquals(user.getCreatedBy(), result.getCreatedBy());
        assertEquals(user.getCreatedDate().toLocalDate(), result.getCreatedDate());
        assertEquals(user.getDateOfBirth(), result.getDateOfBirth());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getFullName(), result.getFullName());
        assertEquals(user.getLastModifiedBy(), result.getLastModifiedBy());
        assertEquals(user.getLastModifiedDate().toLocalDate(), result.getLastModifiedDate());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.getPhone(), result.getPhone());
        assertEquals(user.getPhoto(), result.getPhoto());
        assertEquals(user.getRole().name(), result.getRole());
        assertEquals(user.getStatus().name(), result.getStatus());
    }

    @Test
    void toUserEntity_WithNullUserDto_ReturnsNull() {
        // Act
        User result = userMapper.toUserEntity(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toUserEntity_WithUserDto_ReturnsUser() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setAddress("123 Main St");
        userDto.setCreatedBy("admin");
        userDto.setCreatedDate(LocalDate.now());
        userDto.setDateOfBirth(LocalDate.of(1990, 1, 1));
        userDto.setEmail("test@example.com");
        userDto.setFullName("Test User");
        userDto.setLastModifiedBy("admin");
        userDto.setLastModifiedDate(LocalDate.now());
        userDto.setPassword("password");
        userDto.setPhone("123456789");
        userDto.setPhoto("photo.jpg");
        userDto.setRole(Role.CUSTOMER.name());
        userDto.setStatus(Status.ACTIVE.name());

        // Act
        User result = userMapper.toUserEntity(userDto);

        // Assert
        assertNotNull(result);
        assertEquals(userDto.getAddress(), result.getAddress());
        assertEquals(userDto.getCreatedBy(), result.getCreatedBy());
        assertEquals(userDto.getCreatedDate().atStartOfDay(), result.getCreatedDate());
        assertEquals(userDto.getDateOfBirth(), result.getDateOfBirth());
        assertEquals(userDto.getEmail(), result.getEmail());
        assertEquals(userDto.getFullName(), result.getFullName());
        assertEquals(userDto.getLastModifiedBy(), result.getLastModifiedBy());
        assertEquals(userDto.getLastModifiedDate().atStartOfDay(), result.getLastModifiedDate());
        assertEquals(userDto.getPassword(), result.getPassword());
        assertEquals(userDto.getPhone(), result.getPhone());
        assertEquals(userDto.getPhoto(), result.getPhoto());
        assertEquals(Role.valueOf(userDto.getRole()), result.getRole());
        assertEquals(Status.valueOf(userDto.getStatus()), result.getStatus());
    }
}

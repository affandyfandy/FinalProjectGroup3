package com.hotel.auth_service.controller;

import com.hotel.auth_service.criteria.UserSearchCriteria;
import com.hotel.auth_service.dto.UserDto;
import com.hotel.auth_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private static final String USER_NOT_FOUND = "User not found";

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<UserDto>> getAllUsers(Pageable pageable, UserSearchCriteria criteria) {
        Page<UserDto> userDtos = userService.getAllUsers(pageable, criteria);
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") String id) {
        UserDto userDto = userService.getUserByEmail(id)
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto userDto) {
        UserDto createdUser = userService.createUser(userDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") String id, @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(id, userDto)
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<UserDto> changeUserPassword(@PathVariable("id") String id, @RequestBody String password) {
        UserDto updatedUser = userService.changeUserPassword(id, password)
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<UserDto> toggleUserStatus(@PathVariable("id") String id) {
        UserDto updatedUser = userService.toggleUserStatus(id)
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}

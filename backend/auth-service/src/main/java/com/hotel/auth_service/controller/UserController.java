package com.hotel.auth_service.controller;

<<<<<<< HEAD
import com.hotel.auth_service.dto.UserDto;
import com.hotel.auth_service.service.UserService;
=======
>>>>>>> 62f325808f0c97b8073ef7174c0e413b0576a06d
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.*;
=======
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.auth_service.dto.UserDto;
import com.hotel.auth_service.service.UserService;
>>>>>>> 62f325808f0c97b8073ef7174c0e413b0576a06d

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
    public ResponseEntity<Page<UserDto>> getAllUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                                        @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDto> userDtos = userService.getAllUsers(pageable);
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") String id) {
        UserDto userDto = userService.getUserByEmail(id)
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
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

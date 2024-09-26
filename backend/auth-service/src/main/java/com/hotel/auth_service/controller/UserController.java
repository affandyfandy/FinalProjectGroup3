package com.hotel.auth_service.controller;

import com.hotel.auth_service.criteria.UserSearchCriteria;
import com.hotel.auth_service.dto.UserDto;
import com.hotel.auth_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private static final String USER_NOT_FOUND = "User not found";
    private final Path rootLocation = Paths.get("src/main/resources/static/images");
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

    @PostMapping("/{id}/photo")
    public ResponseEntity<?> uploadUserPhoto(@PathVariable("id") String id, @RequestParam("file") MultipartFile file) {
        try {
            UserDto updatedUser = userService.uploadUserPhoto(id, file);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> getUserPhoto(@PathVariable String filename) {
        try {
            Path filePath = rootLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"" )
                        .body(resource);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

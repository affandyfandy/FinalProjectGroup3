package com.hotel.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String email;
    private String password;
    private String role;
    private String fullName;
    private String phone;
    private LocalDate dateOfBirth;
    private String photo;
    private String address;
    private String status;
}

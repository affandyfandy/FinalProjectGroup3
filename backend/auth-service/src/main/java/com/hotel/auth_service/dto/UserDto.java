package com.hotel.auth_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotBlank(message = "Email should not be blank")
    private String email;

    @NotBlank(message = "Password should not be blank")
    private String password;
    private String role;
    private String fullName;
    private String phone;
    private LocalDate dateOfBirth;
    private String photo;
    private String address;
    private String status;
    private String createdBy;
    private String lastModifiedBy;
    private LocalDate createdDate;
    private LocalDate lastModifiedDate;
}

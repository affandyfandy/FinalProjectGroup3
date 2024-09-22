package com.hotel.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDto {
    String email;
    String fullName;
    String phone;
    String photo;
    String dateOfBirth;
    String address;
}

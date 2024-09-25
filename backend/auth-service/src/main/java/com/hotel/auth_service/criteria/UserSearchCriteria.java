package com.hotel.auth_service.criteria;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserSearchCriteria {
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private String status;
    private String role;
    private String createdBy;
    private String lastModifiedBy;
    private LocalDate createdDate;
    private LocalDate lastModifiedDate;
}

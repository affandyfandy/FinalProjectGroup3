package com.hotel.auth_service.entity;

import com.hotel.auth_service.audit.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User extends Auditable<String> {
    @Id
    @Column(unique = true)
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email should not be blank")
    private String email;

    @NotBlank(message = "Password should not be blank")
    @Size(min = 8, message = "Password should have at least 8 characters")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @NotBlank(message = "Full name should not be blank")
    private String fullName;

    @Pattern(regexp = "^\\+62[0-9]{9,14}$", message = "Phone should be valid")
    private String phone;

    @Past(message = "Date of birth should be in the past")
    private LocalDate dateOfBirth;

    private String photo;

    private String address;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Override
    public String toString() {
        return "user{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", photo='" + photo + '\'' +
                ", address='" + address + '\'' +
                ", status=" + status +
                '}';
    }
}

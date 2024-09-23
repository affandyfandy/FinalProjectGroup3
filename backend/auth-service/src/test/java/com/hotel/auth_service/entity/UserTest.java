package com.hotel.auth_service.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    @Test
    void testToString() {
        // Arrange
        String email = "alif@example.com";
        String password = "12345678";
        Role role = Role.CUSTOMER; // Assuming Role is an enum
        String fullName = "Alif";
        String phone = "+62123456789";
        LocalDate dateOfBirth = LocalDate.of(1995, 9, 19);
        String photo = "https://example.com/photo.jpg";
        String address = "Indonesia";
        Status status = Status.ACTIVE; // Assuming Status is an enum

        // Create a User instance with all the values
        User user = new User(email, password, role, fullName, phone, dateOfBirth, photo, address, status);

        // Expected toString result
        String expected = "user{" +
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

        // Act
        String actual = user.toString();

        // Assert
        assertEquals(expected, actual);
    }
}

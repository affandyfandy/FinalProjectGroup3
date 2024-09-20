package com.hotel.auth_service.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthUserTest {
    private User mockUser;
    private AuthUser authUser;

    @BeforeEach
    void setUp() {
        mockUser = mock(User.class);
        authUser = new AuthUser(mockUser);
    }

    @Test
    void testGetAuthorities() {
        Role role = Role.CUSTOMER;
        when(mockUser.getRole()).thenReturn(role);

        Collection<? extends GrantedAuthority> authorities = authUser.getAuthorities();

        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority(role.name())));
    }

    @Test
    void testIsEnabled() {
        boolean isEnabled = authUser.isEnabled();

        assertTrue(isEnabled);
    }

    @Test
    void testIsCredentialsNonExpired() {
        boolean isCredentialsNonExpired = authUser.isCredentialsNonExpired();

        assertTrue(isCredentialsNonExpired);
    }

    @Test
    void testIsAccountNonLocked() {
        boolean isAccountNonLocked = authUser.isAccountNonLocked();

        assertTrue(isAccountNonLocked);
    }

    @Test
    void testIsAccountNonExpired() {
        boolean isAccountNonExpired = authUser.isAccountNonExpired();

        assertTrue(isAccountNonExpired);
    }

    @Test
    void testGetUsername() {
        String email = "alif@example.com";
        when(mockUser.getEmail()).thenReturn(email);

        String username = authUser.getUsername();

        assertEquals(email, username);
    }

    @Test
    void testGetPassword() {
        String password = "securepassword";
        when(mockUser.getPassword()).thenReturn(password);

        String retrievedPassword = authUser.getPassword();

        assertEquals(password, retrievedPassword);
    }
}

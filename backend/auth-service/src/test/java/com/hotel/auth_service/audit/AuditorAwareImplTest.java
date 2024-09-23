package com.hotel.auth_service.audit;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuditorAwareImplTest {
    private AuditorAwareImpl auditorAware;

    @BeforeEach
    void setUp() {
        auditorAware = new AuditorAwareImpl();
    }

    @Test
    void testGetCurrentAuditor_WithLoggedUserHeader() {
        try (MockedStatic<RequestContextHolder> mockedRequestContextHolder = mockStatic(RequestContextHolder.class)) {
            HttpServletRequest request = mock(HttpServletRequest.class);
            ServletRequestAttributes attributes = mock(ServletRequestAttributes.class);

            when(RequestContextHolder.getRequestAttributes()).thenReturn(attributes);
            when(attributes.getRequest()).thenReturn(request);
            when(request.getHeader("Logged-User")).thenReturn("testuser@example.com");

            Optional<String> currentAuditor = auditorAware.getCurrentAuditor();

            assertTrue(currentAuditor.isPresent());
            assertEquals("testuser@example.com", currentAuditor.get());

            verify(request, times(1)).getHeader("Logged-User");
        }
    }

    @Test
    void testGetCurrentAuditor_NoLoggedUserHeader() {
        try (MockedStatic<RequestContextHolder> mockedRequestContextHolder = mockStatic(RequestContextHolder.class)) {
            HttpServletRequest request = mock(HttpServletRequest.class);
            ServletRequestAttributes attributes = mock(ServletRequestAttributes.class);

            when(RequestContextHolder.getRequestAttributes()).thenReturn(attributes);
            when(attributes.getRequest()).thenReturn(request);
            when(request.getHeader("Logged-User")).thenReturn(null);

            Optional<String> currentAuditor = auditorAware.getCurrentAuditor();

            assertFalse(currentAuditor.isPresent());

            verify(request, times(1)).getHeader("Logged-User");
        }
    }

    @Test
    void testGetCurrentAuditor_NoRequestAttributes() {
        try (MockedStatic<RequestContextHolder> mockedRequestContextHolder = mockStatic(RequestContextHolder.class)) {
            when(RequestContextHolder.getRequestAttributes()).thenReturn(null);

            Optional<String> currentAuditor = auditorAware.getCurrentAuditor();

            assertFalse(currentAuditor.isPresent());
        }
    }
}

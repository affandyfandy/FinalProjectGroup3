package com.hotel.reservation_service.audit;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuditorAwareImplTest {

    private AuditorAwareImpl auditorAware;

    @BeforeEach
    void setUp() {
        auditorAware = new AuditorAwareImpl();
    }

    @Test
    void testGetCurrentAuditor_WithLoggedUserHeader() {
        // Mocking RequestContextHolder to simulate a request
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        ServletRequestAttributes mockAttributes = mock(ServletRequestAttributes.class);
        when(mockRequest.getHeader("Logged-User")).thenReturn("user@example.com");
        when(mockAttributes.getRequest()).thenReturn(mockRequest);
        try (MockedStatic<RequestContextHolder> mockRequestContextHolder = mockStatic(RequestContextHolder.class)) {
            mockRequestContextHolder.when(RequestContextHolder::getRequestAttributes).thenReturn(mockAttributes);
            Optional<String> auditor = auditorAware.getCurrentAuditor();
            assertEquals(Optional.of("user@example.com"), auditor);
        }
    }

    @Test
    void testGetCurrentAuditor_WithoutLoggedUserHeader() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        ServletRequestAttributes mockAttributes = mock(ServletRequestAttributes.class);

        when(mockRequest.getHeader("Logged-User")).thenReturn(null);
        when(mockAttributes.getRequest()).thenReturn(mockRequest);

        try (MockedStatic<RequestContextHolder> mockRequestContextHolder = mockStatic(RequestContextHolder.class)) {
            mockRequestContextHolder.when(RequestContextHolder::getRequestAttributes).thenReturn(mockAttributes);
            Optional<String> auditor = auditorAware.getCurrentAuditor();
            assertEquals(Optional.empty(), auditor);
        }
    }

    @Test
    void testGetCurrentAuditor_NoRequestAttributes() {
        try (MockedStatic<RequestContextHolder> mockRequestContextHolder = mockStatic(RequestContextHolder.class)) {
            mockRequestContextHolder.when(RequestContextHolder::getRequestAttributes).thenReturn(null);
            Optional<String> auditor = auditorAware.getCurrentAuditor();
            assertEquals(Optional.empty(), auditor);
        }
    }
}

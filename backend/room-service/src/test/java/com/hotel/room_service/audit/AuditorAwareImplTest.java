package com.hotel.room_service.audit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AuditorAwareImplTest {

    @InjectMocks
    private AuditorAwareImpl auditorAware;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetCurrentAuditor_WithHeader() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Logged-User", "testUser@example.com");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Optional<String> auditor = auditorAware.getCurrentAuditor();
        assertTrue(auditor.isPresent());
        assertEquals("testUser@example.com", auditor.get());
    }

    @Test
    void testGetCurrentAuditor_NoHeader() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Optional<String> auditor = auditorAware.getCurrentAuditor();
        assertTrue(auditor.isEmpty());
    }
}

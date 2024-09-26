package com.hotel.room_service.audit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

class AuditableTest {

    private Auditable<String> auditable;

    @BeforeEach
    void setUp() {
        auditable = new Auditable<String>() {};
        auditable.setCreatedBy("admin");
        auditable.setCreatedDate(LocalDateTime.now().minusDays(1));
        auditable.setLastModifiedBy("user");
        auditable.setLastModifiedDate(LocalDateTime.now());
    }

    @Test
    void testGetCreatedBy() {
        assertEquals("admin", auditable.getCreatedBy());
    }

    @Test
    void testGetCreatedDate() {
        assertNotNull(auditable.getCreatedDate());
    }

    @Test
    void testGetLastModifiedBy() {
        assertEquals("user", auditable.getLastModifiedBy());
    }

    @Test
    void testGetLastModifiedDate() {
        assertNotNull(auditable.getLastModifiedDate());
    }

    @Test
    void testSetCreatedBy() {
        auditable.setCreatedBy("newAdmin");
        assertEquals("newAdmin", auditable.getCreatedBy());
    }

    @Test
    void testSetLastModifiedBy() {
        auditable.setLastModifiedBy("newUser");
        assertEquals("newUser", auditable.getLastModifiedBy());
    }
}

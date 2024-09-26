package com.hotel.reservation_service.specification;

import com.hotel.reservation_service.entity.Reservation;
import com.hotel.reservation_service.entity.ReservationStatus;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SpecificationHashTest {

    @Test
    void testHasStatus() {
        Specification<Reservation> specification = ReservationSpecification.hasStatus(ReservationStatus.CONFIRMED);
        assertNotNull(specification);
    }

    @Test
    void testHasUserId() {
        Specification<Reservation> specification = ReservationSpecification.hasUserId("user123");
        assertNotNull(specification);
    }

    @Test
    void testHasCheckInDateAfter() {
        Specification<Reservation> specification = ReservationSpecification.hasCheckInDateAfter(LocalDateTime.now());
        assertNotNull(specification);
    }

    @Test
    void testHasCheckOutDateBefore() {
        Specification<Reservation> specification = ReservationSpecification.hasCheckOutDateBefore(LocalDateTime.now());
        assertNotNull(specification);
    }
}

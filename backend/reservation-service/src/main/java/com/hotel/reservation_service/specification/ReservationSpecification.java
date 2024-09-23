package com.hotel.reservation_service.specification;

import com.hotel.reservation_service.entity.Reservation;
import com.hotel.reservation_service.entity.ReservationStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class ReservationSpecification {

    public static Specification<Reservation> hasStatus(ReservationStatus status) {
        return (root, query, criteriaBuilder) -> 
            status == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Reservation> hasUserId(String userId) {
        return (root, query, criteriaBuilder) -> 
            userId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("userId"), userId);
    }

    public static Specification<Reservation> hasCheckInDateAfter(LocalDateTime checkInDate) {
        return (root, query, criteriaBuilder) -> 
            checkInDate == null ? criteriaBuilder.conjunction() : criteriaBuilder.greaterThanOrEqualTo(root.get("checkInDate"), checkInDate);
    }

    public static Specification<Reservation> hasCheckOutDateBefore(LocalDateTime checkOutDate) {
        return (root, query, criteriaBuilder) -> 
            checkOutDate == null ? criteriaBuilder.conjunction() : criteriaBuilder.lessThanOrEqualTo(root.get("checkOutDate"), checkOutDate);
    }
}

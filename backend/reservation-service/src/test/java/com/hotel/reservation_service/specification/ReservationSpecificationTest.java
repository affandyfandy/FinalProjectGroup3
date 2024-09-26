package com.hotel.reservation_service.specification;

import com.hotel.reservation_service.entity.Reservation;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationSpecificationTest {

    private Root<Reservation> root;
    private CriteriaQuery<?> query;
    private CriteriaBuilder criteriaBuilder;
    private Path<Object> pathMock;

    @BeforeEach
    void setUp() {
        // Mocking JPA criteria objects
        root = Mockito.mock(Root.class);
        query = Mockito.mock(CriteriaQuery.class);
        criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        pathMock = Mockito.mock(Path.class);

        // Mock behavior for root.get() to return a Path object
        when(root.get(anyString())).thenReturn(pathMock);
    }

    @Test
    void testHasStatus_whenStatusIsNull() {
        // Test when status is null, should return conjunction (true predicate)
        when(criteriaBuilder.conjunction()).thenReturn(mock(Predicate.class));

        Specification<Reservation> specification = ReservationSpecification.hasStatus(null);
        Predicate predicate = specification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder, times(1)).conjunction();
        assertNotNull(predicate);
    }


    @Test
    void testHasUserId_whenUserIdIsNull() {
        // Test when userId is null, should return conjunction (true predicate)
        when(criteriaBuilder.conjunction()).thenReturn(mock(Predicate.class));

        Specification<Reservation> specification = ReservationSpecification.hasUserId(null);
        Predicate predicate = specification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder, times(1)).conjunction();
        assertNotNull(predicate);
    }


    @Test
    void testHasCheckInDateAfter_whenCheckInDateIsNull() {
        // Test when checkInDate is null, should return conjunction (true predicate)
        when(criteriaBuilder.conjunction()).thenReturn(mock(Predicate.class));

        Specification<Reservation> specification = ReservationSpecification.hasCheckInDateAfter(null);
        Predicate predicate = specification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder, times(1)).conjunction();
        assertNotNull(predicate);
    }

    @Test
    void testHasCheckOutDateBefore_whenCheckOutDateIsNull() {
        // Test when checkOutDate is null, should return conjunction (true predicate)
        when(criteriaBuilder.conjunction()).thenReturn(mock(Predicate.class));

        Specification<Reservation> specification = ReservationSpecification.hasCheckOutDateBefore(null);
        Predicate predicate = specification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder, times(1)).conjunction();
        assertNotNull(predicate);
    }

}

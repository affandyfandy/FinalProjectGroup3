package com.hotel.auth_service.spesification;

import com.hotel.auth_service.criteria.UserSearchCriteria;
import com.hotel.auth_service.entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class UserSpesification implements Specification<User> {

    private final UserSearchCriteria criteria;

    public UserSpesification(UserSearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.conjunction();

        if (criteria.getEmail() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("email"), "%" + criteria.getEmail() + "%"));
        }

        if (criteria.getFullName() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("fullName"), "%" + criteria.getFullName() + "%"));
        }

        if (criteria.getPhone() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("phone"), criteria.getPhone()));
        }

        if (criteria.getAddress() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("address"), "%" + criteria.getAddress() + "%"));
        }

        if (criteria.getStatus() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("status"), criteria.getStatus()));
        }

        if (criteria.getRole() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("role"), criteria.getRole()));
        }

        if (criteria.getCreatedBy() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("createdBy"), "%" + criteria.getCreatedBy() + "%"));
        }

        if (criteria.getLastModifiedBy() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("lastModifiedBy"), "%" + criteria.getLastModifiedBy() + "%"));
        }

        if (criteria.getCreatedDate() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(
                    criteriaBuilder.function("DATE", LocalDate.class, root.get("createdDate")),
                    criteria.getCreatedDate()
            ));
        }

        if (criteria.getLastModifiedDate() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(
                    criteriaBuilder.function("DATE", LocalDate.class, root.get("lastModifiedDate")),
                    criteria.getLastModifiedDate()
            ));
        }

        return predicate;
    }
}

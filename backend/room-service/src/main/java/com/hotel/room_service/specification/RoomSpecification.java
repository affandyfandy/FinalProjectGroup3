package com.hotel.room_service.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import com.hotel.room_service.criteria.RoomSearchCriteria;
import com.hotel.room_service.entity.Room;

public class RoomSpecification implements Specification<Room>  {
    private final RoomSearchCriteria criteria;

    public RoomSpecification(RoomSearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Room> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.conjunction();

        if (criteria.getRoomNumber() != null){
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("roomNumber"), "%" + criteria.getRoomNumber() + "%"));
        }

        if (criteria.getCapacity() != null){
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("capacity"), "%" + criteria.getCapacity() + "%"));
        }

        if (criteria.getRoomType() != null){
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("roomType"), "%" + criteria.getRoomType() + "%"));
        }

        if (criteria.getPrice() != null){
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("price"), "%" + criteria.getPrice() + "%"));
        }

        if (criteria.getStatus() != null){
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("status"), "%" + criteria.getStatus() + "%"));
        }
        return predicate;
    }
    
}

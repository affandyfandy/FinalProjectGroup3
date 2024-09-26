package com.hotel.room_service.criteria;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomSearchCriteria {
    private String roomNumber;
    private Integer capacity;
    private String roomType;
    private BigDecimal price;
    private String status;
}

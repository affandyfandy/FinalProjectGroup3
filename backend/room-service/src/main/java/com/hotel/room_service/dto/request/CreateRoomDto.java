package com.hotel.room_service.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateRoomDto {
    private String roomType;
    private Integer capacity;
    private String status;
    private BigDecimal price;
    private UUID hotelId;
    private String photo;
    private String facility;
}

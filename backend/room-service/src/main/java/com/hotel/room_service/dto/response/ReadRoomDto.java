package com.hotel.room_service.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import java.util.ArrayList;

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
public class ReadRoomDto {
    private UUID id;
    private String roomNumber;
    private String roomType;
    private Integer capacity;
    private String status;
    private BigDecimal price;
    private String photo;
    private ArrayList<String> facility;
    private String createdBy;
    private String lastModifiedBy;
    private LocalDate createdDate;
    private LocalDate lastModifiedDate;
}
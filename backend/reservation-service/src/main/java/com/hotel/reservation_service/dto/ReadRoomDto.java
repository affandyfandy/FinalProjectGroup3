package com.hotel.reservation_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.hotel.reservation_service.entity.ReservationStatus;

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
    private LocalDateTime reservationDate;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private ReservationStatus status;
    private String userId;
    private String roomId;
    private BigDecimal amount;
}
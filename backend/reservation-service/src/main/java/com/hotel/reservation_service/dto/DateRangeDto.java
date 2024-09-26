package com.hotel.reservation_service.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateRangeDto {
    private LocalDate from;
    private LocalDate to;
}

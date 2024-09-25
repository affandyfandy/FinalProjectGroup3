package com.hotel.room_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@FeignClient(name = "reservation-service", url = "http://localhost:8082")
public interface ReservationServiceClient {

    @GetMapping("/api/v1/reservations/unavailable-room-ids")
    Page<UUID> getUnavailableRoomIds(@RequestParam List<UUID> roomIds,
                                     @RequestParam LocalDate checkInDate,
                                     @RequestParam LocalDate checkOutDate,
                                     Pageable pageable);
}

package com.hotel.reservation_service.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.reservation_service.dto.DateRangeDto;
import com.hotel.reservation_service.entity.Reservation;
import com.hotel.reservation_service.entity.ReservationStatus;
import com.hotel.reservation_service.service.ReservationService;

@RestController
@RequestMapping("api/v1/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/unavailable-room-ids")
    public Page<UUID> getUnavailableRoomIds(
            @RequestParam(required = false, defaultValue = "") List<UUID> roomIds,
            @RequestParam(required = false) LocalDate checkInDate,
            @RequestParam(required = false) LocalDate checkOutDate,
            Pageable pageable) {
        return reservationService.getUnavailableRoomIds(roomIds, checkInDate, checkOutDate, pageable);
    }

    @GetMapping
    public Page<Reservation> getAllReservations(
            @PageableDefault(size = 10, sort = "reservationDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return reservationService.getAllReservations(pageable);
    }

    @GetMapping("/user/{userId}")
    public List<Reservation> getReservationsByUserId(@PathVariable String userId) {
        return reservationService.getReservationsByUserId(userId);
    }

    @GetMapping("/sorted")
    public List<Reservation> getAllReservationsSorted(
            @RequestParam(defaultValue = "reservationDate") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
        Sort sort = Sort.by(direction, sortBy);
        return reservationService.getAllReservations(sort);
    }

    @GetMapping("/{id}")
    public Reservation getReservationById(@PathVariable UUID id) {
        return reservationService.getReservationById(id);
    }

    @PostMapping
    public Reservation createReservation(@RequestBody Reservation reservation) {
        return reservationService.createReservation(reservation);
    }

    @PutMapping("/{id}")
    public Reservation updateReservation(@PathVariable UUID id, @RequestBody Reservation reservation) {
        return reservationService.updateReservation(id, reservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable UUID id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public Page<Reservation> searchReservations(
        @RequestParam(required = false) ReservationStatus status,
        @RequestParam(required = false) String userId,
        @RequestParam(required = false) LocalDateTime checkInDate,
        @RequestParam(required = false) LocalDateTime checkOutDate,
        Pageable pageable
    ) {
        return reservationService.searchReservations(status, userId, checkInDate, checkOutDate, pageable);
    }

    @GetMapping("/export/customer/{userId}")
    public ResponseEntity<byte[]> exportCustomerReservationsToPdf(@PathVariable String userId) {
        byte[] pdfContent = reservationService.generateCustomerReservationPdf(userId);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=customer_reservations.pdf");
        return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
    }

    @GetMapping("/export/admin")
    public ResponseEntity<byte[]> exportAdminReportToPdf(@RequestParam(required = false) String filter) {
        byte[] pdfContent = reservationService.generateAdminReportPdf(filter);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=admin_reservations_report.pdf");
        return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
    }


    @GetMapping("/unavailable-dates/{roomId}")
    public List<DateRangeDto> getUnavailableDatesByRoomId(@PathVariable UUID roomId) {
        return reservationService.findUnavailableDateRangesByRoomId(roomId);
    }
}

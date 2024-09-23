package com.hotel.reservation_service.controller;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

import com.hotel.reservation_service.entity.Reservation;
import com.hotel.reservation_service.entity.ReservationStatus;
import com.hotel.reservation_service.service.ReservationService;
import com.hotel.reservation_service.util.PDFGenerator;

@RestController
@RequestMapping("api/v1/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public Page<Reservation> getAllReservations(
            @PageableDefault(size = 10, sort = "reservationDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return reservationService.getAllReservations(pageable);
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

    @GetMapping("/export/user/{userId}")
    public ResponseEntity<InputStreamResource> exportUserReservationsToPDF(@PathVariable String userId) {
        List<Reservation> reservations = reservationService.getReservationsByUserId(userId);

        ByteArrayInputStream bis = PDFGenerator.generateUserReservationPDF(reservations);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=user_reservations.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @GetMapping("/export/admin")
    public ResponseEntity<InputStreamResource> exportAllReservationsToPDF() {
        List<Reservation> reservations = reservationService.getAllReservations(Sort.by(Sort.Direction.DESC, "reservationDate"));

        ByteArrayInputStream bis = PDFGenerator.generateAdminReservationReportPDF(reservations);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=admin_reservation_report.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}

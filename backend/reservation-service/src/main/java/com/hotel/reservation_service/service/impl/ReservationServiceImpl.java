package com.hotel.reservation_service.service.impl;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.hotel.reservation_service.dto.DateRangeDto;
import com.hotel.reservation_service.entity.Reservation;
import com.hotel.reservation_service.entity.ReservationStatus;
import com.hotel.reservation_service.repository.ReservationRepository;
import com.hotel.reservation_service.service.ReservationService;
import com.hotel.reservation_service.specification.ReservationSpecification;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final TemplateEngine templateEngine;

    public ReservationServiceImpl(ReservationRepository reservationRepository, TemplateEngine templateEngine) {
        this.reservationRepository = reservationRepository;
        this.templateEngine = templateEngine;
    }

    @Override
    public Page<UUID> getUnavailableRoomIds(List<UUID> roomIds, LocalDate checkInDate, LocalDate checkOutDate, Pageable pageable) {
        Page<Reservation> conflictingReservations = reservationRepository
                .findConflictingReservations(roomIds, checkInDate, checkOutDate, pageable);

        List<UUID> unavailableRoomIds = conflictingReservations.stream()
                .map(Reservation::getRoomId)
                .distinct()
                .toList();

        return new PageImpl<>(unavailableRoomIds, pageable, conflictingReservations.getTotalElements());
    }

    @Override
    public Page<Reservation> getAllReservations(Pageable pageable) {
        return reservationRepository.findAll(pageable); 
    }

    @Override
    public List<Reservation> getAllReservations(Sort sort) {
        return reservationRepository.findAll(sort); 
    }

    @Override
    public Page<Reservation> searchReservations(ReservationStatus status, String userId, LocalDateTime checkInDate, LocalDateTime checkOutDate, Pageable pageable) {
        Specification<Reservation> specification = Specification
            .where(ReservationSpecification.hasStatus(status))
            .and(ReservationSpecification.hasUserId(userId))
            .and(ReservationSpecification.hasCheckInDateAfter(checkInDate))
            .and(ReservationSpecification.hasCheckOutDateBefore(checkOutDate));

        return reservationRepository.findAll(specification, pageable);
    }

    @Override
    public Reservation getReservationById(UUID id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
    }

    @Override
    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation updateReservation(UUID id, Reservation reservation) {
        Reservation existingReservation = getReservationById(id);
        existingReservation.setCheckInDate(reservation.getCheckInDate());
        existingReservation.setCheckOutDate(reservation.getCheckOutDate());
        existingReservation.setStatus(reservation.getStatus());
        return reservationRepository.save(existingReservation);
    }

    @Override
    public void deleteReservation(UUID id) {
        Reservation reservation = getReservationById(id);
        reservationRepository.delete(reservation);
    }

    @Override
    public byte[] generateCustomerReservationPdf(String userId) {
        List<Reservation> reservations = reservationRepository.findByUserId(userId);
        Context context = new Context();
        context.setVariable("reservations", reservations);

        String htmlContent = templateEngine.process("reservation_template", context);
        return convertHtmlToPdf(htmlContent);
    }

    @Override
    public byte[] generateAdminReportPdf(String filter) {
        List<Reservation> reservations;

        if (filter == null || filter.isEmpty()) {
            reservations = reservationRepository.findAll();
        } else {
            reservations = reservationRepository.findAll(); 
        }

        Context context = new Context();
        context.setVariable("reservations", reservations);

        String htmlContent = templateEngine.process("admin_report_template", context);
        return convertHtmlToPdf(htmlContent);
    }


    private byte[] convertHtmlToPdf(String htmlContent) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }

    @Override
    public List<DateRangeDto> findUnavailableDateRangesByRoomId(UUID roomId) {
        return reservationRepository.findUnavailableDateRangesByRoomId(roomId);
    }
}

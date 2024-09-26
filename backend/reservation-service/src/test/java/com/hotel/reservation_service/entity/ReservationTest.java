package com.hotel.reservation_service.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {

    private Reservation reservation;
    private final UUID reservationId = UUID.randomUUID();
    private final LocalDate checkInDate = LocalDate.of(2023, 10, 1);
    private final LocalDate checkOutDate = LocalDate.of(2023, 10, 5);
    private final UUID roomId = UUID.randomUUID();
    private final String userId = "user123";
    private final BigDecimal amount = new BigDecimal("100.00");

    @BeforeEach
    void setUp() {
        reservation = Reservation.builder()
                .id(reservationId)
                .checkInDate(checkInDate)
                .checkOutDate(checkOutDate)
                .roomId(roomId)
                .userId(userId)
                .amount(amount)
                .status(ReservationStatus.CONFIRMED)
                .build();
    }

    @Test
    void testReservationBuilderAndGetters() {
        // Validate builder values and getters
        assertEquals(reservationId, reservation.getId());
        assertEquals(checkInDate, reservation.getCheckInDate());
        assertEquals(checkOutDate, reservation.getCheckOutDate());
        assertEquals(roomId, reservation.getRoomId());
        assertEquals(userId, reservation.getUserId());
        assertEquals(amount, reservation.getAmount());
        assertEquals(ReservationStatus.CONFIRMED, reservation.getStatus());
    }

    @Test
    void testSetters() {
        // Test setters by updating values and verifying changes
        LocalDate newCheckInDate = LocalDate.of(2023, 11, 1);
        LocalDate newCheckOutDate = LocalDate.of(2023, 11, 5);
        BigDecimal newAmount = new BigDecimal("200.00");

        reservation.setCheckInDate(newCheckInDate);
        reservation.setCheckOutDate(newCheckOutDate);
        reservation.setAmount(newAmount);
        reservation.setStatus(ReservationStatus.CANCELED);

        assertEquals(newCheckInDate, reservation.getCheckInDate());
        assertEquals(newCheckOutDate, reservation.getCheckOutDate());
        assertEquals(newAmount, reservation.getAmount());
        assertEquals(ReservationStatus.CANCELED, reservation.getStatus());
    }

    @Test
    void testPrePersist_setsReservationDate() {
        // Simulate the pre-persist behavior
        reservation.prePersist();
        assertEquals(LocalDate.now(), reservation.getReservationDate()); // Ensure the reservationDate is set to the current date
    }

    @Test
    void testEqualsAndHashCode() {
        // Test equals and hashCode
        Reservation reservationCopy = Reservation.builder()
                .id(reservationId)
                .checkInDate(checkInDate)
                .checkOutDate(checkOutDate)
                .roomId(roomId)
                .userId(userId)
                .amount(amount)
                .status(ReservationStatus.CONFIRMED)
                .build();

        assertEquals(reservation, reservationCopy); // Should be equal
        assertEquals(reservation.hashCode(), reservationCopy.hashCode()); // hashCode should also be equal
    }

    @Test
    void testNotEquals() {
        // Test inequality for different objects
        Reservation differentReservation = Reservation.builder()
                .id(UUID.randomUUID())
                .checkInDate(LocalDate.of(2023, 11, 1))
                .checkOutDate(LocalDate.of(2023, 11, 5))
                .roomId(UUID.randomUUID())
                .userId("differentUser")
                .amount(new BigDecimal("300.00"))
                .status(ReservationStatus.CANCELED)
                .build();

        assertNotEquals(reservation, differentReservation); // Should not be equal
    }

    @Test
    void testNoArgsConstructorAndAllArgsConstructor() {
        // Test no-args constructor
        Reservation emptyReservation = new Reservation();
        assertNull(emptyReservation.getId());
        assertNull(emptyReservation.getCheckInDate());
        assertNull(emptyReservation.getCheckOutDate());

        // Test all-args constructor
        Reservation fullReservation = new Reservation(
                reservationId, LocalDate.now(), checkInDate, checkOutDate,
                ReservationStatus.CONFIRMED, userId, roomId, amount);
        assertEquals(reservationId, fullReservation.getId());
        assertEquals(checkInDate, fullReservation.getCheckInDate());
        assertEquals(checkOutDate, fullReservation.getCheckOutDate());
    }

    @Test
    void testToString() {
        // Test toString method (optional, depending on implementation)
        String toStringOutput = reservation.toString();
        assertTrue(toStringOutput.contains("Reservation"));
        assertTrue(toStringOutput.contains("user123"));
        assertTrue(toStringOutput.contains(reservationId.toString()));
    }
}

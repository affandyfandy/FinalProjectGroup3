package com.hotel.room_service.exception;

import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleObjectNotFoundException() {
        // Given
        Long roomId = 1L;
        String entityName = "Room";
        ObjectNotFoundException exception = new ObjectNotFoundException(roomId, entityName);

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleObjectNotFoundException(exception);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertEquals("No row with the given identifier exists: [Room#1]", response.getBody().getMessage());
    }


    @Test
    void testHandleInvalidInputException() {
        // Given
        InvalidInputException exception = new InvalidInputException("Invalid input data");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleInvalidInputException(exception);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertEquals("Invalid input data", response.getBody().getMessage());
    }

    @Test
    void testHandleGenericException() {
        // Given
        Exception exception = new Exception("Internal server error");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGenericException(exception);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getStatus());
        assertEquals("Internal server error", response.getBody().getMessage());
    }
}

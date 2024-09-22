package com.hotel.auth_service.exception;

import com.hotel.auth_service.service.AuthService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashSet;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class GlobalExceptionHandlerTest {
    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @RestController
    static class TestController {

        private final AuthService authService;

        public TestController(AuthService authService) {
            this.authService = authService;
        }

        @GetMapping("/auth-exception")
        public String throwAuthenticationException() {
            doThrow(new BadCredentialsException("Authentication failed")).when(authService).generateToken(any());
            authService.generateToken(null);
            return "should not reach here";
        }

        @GetMapping("/username-not-found")
        public String throwUsernameNotFoundException() {
            doThrow(new UsernameNotFoundException("User not found")).when(authService).registerUser(any());
            authService.registerUser(null);
            return "should not reach here";
        }

        @GetMapping("/general-exception")
        public String throwGeneralException() {
            throw new RuntimeException("General error occurred");
        }

        @GetMapping("/transaction-exception")
        public String throwTransactionSystemException() {
            Path path = mock(Path.class);
            when(path.toString()).thenReturn("field");

            ConstraintViolation<String> violation = mock(ConstraintViolation.class);
            when(violation.getPropertyPath()).thenReturn(path);
            when(violation.getMessage()).thenReturn("must not be null");

            ConstraintViolationException constraintViolationException = new ConstraintViolationException(new HashSet<>(Collections.singleton(violation)));

            throw new TransactionSystemException("Transaction failed", constraintViolationException);
        }

        @GetMapping("/illegal-argument")
        public String throwIllegalArgumentException() {
            throw new IllegalArgumentException("Invalid argument passed");
        }
    }

    @BeforeEach
    public void setup() {

        MockitoAnnotations.openMocks(this);
        TestController testController = new TestController(authService);
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        mockMvc = MockMvcBuilders.standaloneSetup(testController)
                .setControllerAdvice(globalExceptionHandler)
                .build();
    }

    @Test
    void testHandleAuthenticationException() throws Exception {
        mockMvc.perform(get("/auth-exception")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())  // Expect 401 Unauthorized
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Authentication failed"));
    }

    @Test
    void testHandleUsernameNotFoundException() throws Exception {
        mockMvc.perform(get("/username-not-found")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())  // Expect 404 Not Found
                .andExpect(jsonPath("$.error").value("User Not Found"))
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void testHandleGeneralException() throws Exception {
        mockMvc.perform(get("/general-exception")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())  // Expect 500 Internal Server Error
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("General error occurred"));
    }

    @Test
    void testHandleTransactionSystemException() throws Exception {
        mockMvc.perform(get("/transaction-exception")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())  // Expect 400 Bad Request
                .andExpect(jsonPath("$.error").value("Transaction Error"))
                .andExpect(jsonPath("$.message").value("Validation failed for the transaction."))
                .andExpect(jsonPath("$.validationErrors.field").value("must not be null"));
    }

    @Test
    void testHandleIllegalArgumentException() throws Exception {
        mockMvc.perform(get("/illegal-argument")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())  // Expect 400 Bad Request
                .andExpect(content().string("Invalid argument passed"));
    }
}

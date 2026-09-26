package com.mediconnect.shared.error;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

import com.mediconnect.shared.exception.AppointmentConflictException;
import com.mediconnect.shared.exception.BusinessRuleException;
import com.mediconnect.shared.exception.DuplicateResourceException;
import com.mediconnect.shared.exception.LateCancellationException;
import com.mediconnect.shared.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AppointmentConflictException.class)
    ProblemDetail handleAppointmentConflict(AppointmentConflictException ex) { return problem(HttpStatus.CONFLICT, "Appointment conflict", ex.getMessage(), "appointment-conflict"); }
    @ExceptionHandler(LateCancellationException.class)
    ProblemDetail handleLateCancellation(LateCancellationException ex) { return problem(HttpStatus.BAD_REQUEST, "Late cancellation denied", ex.getMessage(), "late-cancellation"); }
    @ExceptionHandler(ResourceNotFoundException.class)
    ProblemDetail handleNotFound(ResourceNotFoundException ex) { return problem(HttpStatus.NOT_FOUND, "Resource not found", ex.getMessage(), "resource-not-found"); }
    @ExceptionHandler(DuplicateResourceException.class)
    ProblemDetail handleDuplicate(DuplicateResourceException ex) { return problem(HttpStatus.CONFLICT, "Duplicate resource", ex.getMessage(), "duplicate-resource"); }
    @ExceptionHandler(BusinessRuleException.class)
    ProblemDetail handleBusinessRule(BusinessRuleException ex) { return problem(HttpStatus.BAD_REQUEST, "Business rule violation", ex.getMessage(), "business-rule"); }
    @ExceptionHandler(BadCredentialsException.class)
    ProblemDetail handleBadCredentials(BadCredentialsException ex) { return problem(HttpStatus.UNAUTHORIZED, "Invalid credentials", ex.getMessage(), "invalid-credentials"); }
    @ExceptionHandler(AccessDeniedException.class)
    ProblemDetail handleAccessDenied(AccessDeniedException ex) { return problem(HttpStatus.FORBIDDEN, "Forbidden", "You do not have permission to perform this operation.", "forbidden"); }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        ProblemDetail detail = problem(HttpStatus.BAD_REQUEST, "Validation failed", "One or more request fields are invalid.", "validation-error");
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.putIfAbsent(error.getField(), error.getDefaultMessage()));
        detail.setProperty("errors", errors);
        return detail;
    }
    @ExceptionHandler(Exception.class)
    ProblemDetail handleUnexpected(Exception ex) { return problem(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error", "The server could not complete the request.", "internal-error"); }
    private ProblemDetail problem(HttpStatus status, String title, String message, String type) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(status, message);
        detail.setTitle(title);
        detail.setType(URI.create("https://mediconnect.local/problems/" + type));
        return detail;
    }
}

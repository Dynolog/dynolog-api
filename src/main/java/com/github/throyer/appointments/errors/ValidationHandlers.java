package com.github.throyer.appointments.errors;

import com.github.throyer.appointments.domain.error.model.SimpleError;
import com.github.throyer.appointments.utils.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
public class ValidationHandlers {

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<SimpleError> badRequest(MethodArgumentNotValidException exception) {
        return SimpleError.of(exception);
    }

    @ResponseStatus(code = UNAUTHORIZED)
    @ExceptionHandler(AccessDeniedException.class)
    public SimpleError unauthorized(AccessDeniedException exception) {
        return new SimpleError(exception.getMessage(), UNAUTHORIZED);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<SimpleError> status(ResponseStatusException exception) {
        return Response.fromException(exception);
    }
}

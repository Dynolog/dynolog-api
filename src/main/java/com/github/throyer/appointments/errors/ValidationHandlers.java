package com.github.throyer.appointments.errors;

import com.github.throyer.appointments.errors.exception.BadRequestException;
import com.github.throyer.appointments.utils.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.DateTimeException;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
public class ValidationHandlers {

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<Error> badRequest(MethodArgumentNotValidException exception) {
        return Error.of(exception);
    }

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public Collection<Error> badRequest(BadRequestException exception) {
        return exception.getErrors();
    }

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(DateTimeParseException.class)
    public Error badRequest(DateTimeParseException exception) {
        return new Error(exception.getCause().getCause().getMessage(), BAD_REQUEST);
    }

    @ResponseStatus(code = UNAUTHORIZED)
    @ExceptionHandler(AccessDeniedException.class)
    public Error unauthorized(AccessDeniedException exception) {
        return new Error(exception.getMessage(), UNAUTHORIZED);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Error> status(ResponseStatusException exception) {
        return Response.fromException(exception);
    }
}

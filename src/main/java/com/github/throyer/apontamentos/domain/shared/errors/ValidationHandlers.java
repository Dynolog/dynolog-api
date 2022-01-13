package com.github.throyer.apontamentos.domain.shared.errors;

import java.util.List;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ValidationHandlers {

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<SimpleError> badRequest(MethodArgumentNotValidException exception) {
        return exception.getBindingResult()
            .getAllErrors()
                .stream()
                    .map((error) -> (new SimpleError((FieldError) error)))
                        .toList();
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<SimpleError> status(ResponseStatusException exception) {
        return ResponseEntity
                .status(exception.getStatus())
                .body(new SimpleError(exception.getReason(), exception.getStatus()));
    }

    @ResponseStatus(code = UNAUTHORIZED)
    @ExceptionHandler(AccessDeniedException.class)
    public SimpleError unauthorized(AccessDeniedException exception) {
        return new SimpleError(exception.getMessage(), UNAUTHORIZED);
    }
}

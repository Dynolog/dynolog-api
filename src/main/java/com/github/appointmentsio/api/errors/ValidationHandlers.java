package com.github.appointmentsio.api.errors;

import static com.github.appointmentsio.api.utils.Constraints.MESSAGES.NOT_AUTHORIZED;
import static com.github.appointmentsio.api.utils.Constraints.MESSAGES.TYPE_MISMATCH_ERROR_MESSAGE;
import static com.github.appointmentsio.api.utils.Messages.message;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import java.time.format.DateTimeParseException;
import java.util.List;

import com.github.appointmentsio.api.errors.exception.BadRequestException;
import com.github.appointmentsio.api.errors.model.ApiError;
import com.github.appointmentsio.api.errors.model.FieldError;
import com.github.appointmentsio.api.errors.model.ValidationError;
import com.github.appointmentsio.api.utils.Response;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ValidationHandlers {

    @ResponseStatus(code = UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationError badRequest(MethodArgumentNotValidException exception) {
        var errors = FieldError.of(exception);
        return new ValidationError(errors);
    }

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ValidationError badRequest(BadRequestException exception) {
        var errors = exception.getErrors();
        return new ValidationError(errors);
    }

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(DateTimeParseException.class)
    public ValidationError badRequest(DateTimeParseException exception) {
        var errors = List.of(new FieldError(exception.getCause().getCause().getMessage()));
        return new ValidationError(errors);
    }

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ValidationError badRequest(MethodArgumentTypeMismatchException exception) {
        var errors = List.of(new FieldError(exception.getParameter().getParameterName(), message(TYPE_MISMATCH_ERROR_MESSAGE)));
        return new ValidationError(errors);
    }

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ValidationError badRequest(MissingServletRequestParameterException exception) {
        var errors = List.of(new FieldError(exception.getParameterName(), exception.getMessage()));
        return new ValidationError(errors);
    }

    @ResponseStatus(code = UNAUTHORIZED)
    @ExceptionHandler(AccessDeniedException.class)
    public ApiError unauthorized(AccessDeniedException exception) {
        return new ApiError(message(NOT_AUTHORIZED), UNAUTHORIZED);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> status(ResponseStatusException exception) {
        return Response.fromException(exception);
    }
}

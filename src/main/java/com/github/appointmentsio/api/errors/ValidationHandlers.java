package com.github.appointmentsio.api.errors;

import com.github.appointmentsio.api.errors.exception.BadRequestException;
import com.github.appointmentsio.api.utils.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.List;

import static com.github.appointmentsio.api.utils.Constraints.MESSAGES.TYPE_MISMATCH_ERROR_MESSAGE;
import static com.github.appointmentsio.api.utils.Messages.message;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ValidationHandlers {

    @ResponseStatus(code = UNPROCESSABLE_ENTITY)
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
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Error badRequest(HttpMessageNotReadableException exception) {
        return new Error(exception.getCause().getMessage(), BAD_REQUEST);
    }

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(DateTimeParseException.class)
    public Error badRequest(DateTimeParseException exception) {
        return new Error(exception.getCause().getCause().getMessage(), BAD_REQUEST);
    }

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public List<Error> badRequest(MethodArgumentTypeMismatchException exception) {
        return List.of(new Error(exception.getParameter().getParameterName(), message(TYPE_MISMATCH_ERROR_MESSAGE)));
    }

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public List<Error> badRequest(MissingServletRequestParameterException exception) {
        return List.of(new Error(exception.getParameterName(), exception.getMessage()));
    }

    @ResponseStatus(code = UNAUTHORIZED)
    @ExceptionHandler(AccessDeniedException.class)
    public List<Error> unauthorized(AccessDeniedException exception) {
        return List.of(new Error(exception.getMessage(), UNAUTHORIZED));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Error> status(ResponseStatusException exception) {
        return Response.fromException(exception);
    }
}

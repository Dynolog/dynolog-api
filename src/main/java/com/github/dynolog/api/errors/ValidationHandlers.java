package com.github.dynolog.api.errors;

import static com.github.dynolog.api.utils.Constants.MESSAGES.NOT_AUTHORIZED;
import static com.github.dynolog.api.utils.Constants.MESSAGES.TYPE_MISMATCH_ERROR_MESSAGE;
import static com.github.dynolog.api.utils.Messages.message;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import java.time.format.DateTimeParseException;
import java.util.List;

import com.github.dynolog.api.errors.exception.BadRequestException;
import com.github.dynolog.api.errors.model.ApiError;
import com.github.dynolog.api.errors.model.FieldError;
import com.github.dynolog.api.errors.model.ValidationError;
import com.github.dynolog.api.utils.Response;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@ApiResponses({
    @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = {
        @Content(schema = @Schema(implementation = ValidationError.class))
    }),
    @ApiResponse(responseCode = "400", description = "Bad Request", content = {
        @Content(schema = @Schema(implementation = ValidationError.class))
    }),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
        @Content(schema = @Schema(implementation = ApiError.class))
    }),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = {
        @Content(schema = @Schema(implementation = ApiError.class))
    })
})
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

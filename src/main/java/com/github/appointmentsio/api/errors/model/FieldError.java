package com.github.appointmentsio.api.errors.model;

import java.util.List;

import org.springframework.web.bind.MethodArgumentNotValidException;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class FieldError {

    @Schema(example = "username")
    private final String field;

    @Schema(example = "Field username is required", required = true)
    private final String message;

    public FieldError(String message) {
        this.field = null;
        this.message = message;
    }

    public FieldError(org.springframework.validation.FieldError error) {
        this.field = error.getField();
        this.message = error.getDefaultMessage();
    }

    public FieldError(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public static List<FieldError> of(MethodArgumentNotValidException exception) {
        return exception.getBindingResult()
                .getAllErrors()
                .stream()
                .map((error) -> (new FieldError((org.springframework.validation.FieldError) error)))
                .toList();
    }
}

package com.github.appointmentsio.api.errors.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

@Getter
@Schema(requiredProperties = {"message"})
public class FieldError {
    private final String field;
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

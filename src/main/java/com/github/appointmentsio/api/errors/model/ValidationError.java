package com.github.appointmentsio.api.errors.model;

import static com.github.appointmentsio.api.utils.Constants.MESSAGES.CHECK_PROPERTY_ERRORS;
import static com.github.appointmentsio.api.utils.Messages.message;

import java.util.Collection;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(requiredProperties = {"message", "errors"})
public class ValidationError {
    public final String message;
    public final Collection<FieldError> errors;

    public ValidationError(Collection<FieldError> errors) {
        this.message = message(CHECK_PROPERTY_ERRORS);
        this.errors = errors;
    }
}

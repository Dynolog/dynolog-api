package com.github.appointmentsio.api.errors.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Collection;

import static com.github.appointmentsio.api.utils.Constraints.MESSAGES.CHECK_PROPERTY_ERRORS;
import static com.github.appointmentsio.api.utils.Messages.message;

@Getter
@Schema(name = "Error", requiredProperties = {"message", "errors"})
public class ValidationError {
    public final String message;
    public final Collection<FieldError> errors;

    public ValidationError(Collection<FieldError> errors) {
        this.message = message(CHECK_PROPERTY_ERRORS);
        this.errors = errors;
    }
}

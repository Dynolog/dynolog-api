package com.github.dynolog.api.errors.model;

import static com.github.dynolog.api.utils.Constants.MESSAGES.CHECK_PROPERTY_ERRORS;
import static com.github.dynolog.api.utils.Messages.message;

import java.util.Collection;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ValidationError {

    @Schema(example = "Check the 'errors' property for more details.", required = true)
    public final String message;

    @Schema(required = true)
    public final Collection<FieldError> errors;

    public ValidationError(Collection<FieldError> errors) {
        this.message = message(CHECK_PROPERTY_ERRORS);
        this.errors = errors;
    }
}

package com.github.appointmentsio.api.errors.exception;

import com.github.appointmentsio.api.errors.ValidationError;

import java.util.ArrayList;
import java.util.Collection;

public class BadRequestException extends RuntimeException {
    Collection<ValidationError> errors;

    public BadRequestException() {
        this.errors = new ArrayList<>();
    }

    public BadRequestException(Collection<ValidationError> errors) {
        this.errors = errors;
    }

    public Collection<ValidationError> getErrors() {
        return errors;
    }

    public void add(ValidationError error) {
        this.errors.add(error);
    }

    public Boolean hasError() {
        return !this.errors.isEmpty();
    }
}

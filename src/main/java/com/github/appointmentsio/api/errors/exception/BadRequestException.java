package com.github.appointmentsio.api.errors.exception;

import java.util.ArrayList;
import java.util.Collection;

import com.github.appointmentsio.api.errors.model.FieldError;

public class BadRequestException extends RuntimeException {
    Collection<FieldError> errors;

    public BadRequestException() {
        this.errors = new ArrayList<>();
    }

    public BadRequestException(Collection<FieldError> errors) {
        this.errors = errors;
    }

    public Collection<FieldError> getErrors() {
        return errors;
    }

    public void add(FieldError error) {
        this.errors.add(error);
    }

    public Boolean hasError() {
        return !this.errors.isEmpty();
    }
}

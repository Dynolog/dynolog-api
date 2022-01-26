package com.github.throyer.appointments.errors.exception;

import com.github.throyer.appointments.errors.Error;

import java.util.ArrayList;
import java.util.Collection;

public class BadRequestException extends RuntimeException {
    Collection<Error> errors;

    public BadRequestException() {
        this.errors = new ArrayList<>();
    }

    public BadRequestException(Collection<Error> errors) {
        this.errors = errors;
    }

    public BadRequestException(String message, Collection<Error> errors) {
        super(message);
        this.errors = errors;
    }

    public BadRequestException(String message, Throwable cause, Collection<Error> errors) {
        super(message, cause);
        this.errors = errors;
    }

    public BadRequestException(Throwable cause, Collection<Error> errors) {
        super(cause);
        this.errors = errors;
    }

    public BadRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Collection<Error> errors) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errors = errors;
    }

    public Collection<Error> getErrors() {
        return errors;
    }

    public void add(Error error) {
        this.errors.add(error);
    }

    public Boolean hasError() {
        return !this.errors.isEmpty();
    }
}

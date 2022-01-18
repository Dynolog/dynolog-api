package com.github.throyer.appointments.domain.error.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

public class SimpleError {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String field;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer status;

    public SimpleError(FieldError error) {
        this.field = error.getField();
        this.message = error.getDefaultMessage();
    }

    public SimpleError(ObjectError error) {
        this.message = error.getDefaultMessage();
    }

    public SimpleError(String filed, String message) {
        this.field = filed;
        this.message = message;
    }

    public SimpleError(String message, Integer status) {
        this.message = message;
        this.status = status;
    }

    public SimpleError(String message, HttpStatus status) {
        this.message = message;
        this.status = status.value();
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public static final List<SimpleError> of(MethodArgumentNotValidException exception) {
        return exception.getBindingResult()
                .getAllErrors()
                .stream()
                .map((error) -> (new SimpleError((FieldError) error)))
                .toList();
    }
}
package com.github.appointmentsio.api.errors;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Schema(requiredProperties = {"message", "status"})
public class Error {
    private final String message;
    private final Integer status;

    public Error(String message, Integer status) {
        this.message = message;
        this.status = status;
    }

    public Error(String message, HttpStatus status) {
        this.message = message;
        this.status = status.value();
    }
}
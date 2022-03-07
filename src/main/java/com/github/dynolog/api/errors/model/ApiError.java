package com.github.dynolog.api.errors.model;

import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ApiError {
    @Schema(example = "User not found", required = true)
    private final String message;

    @Schema(example = "404", required = true)
    private final Integer status;

    public ApiError(String message, Integer status) {
        this.message = message;
        this.status = status;
    }

    public ApiError(String message, HttpStatus status) {
        this.message = message;
        this.status = status.value();
    }
}
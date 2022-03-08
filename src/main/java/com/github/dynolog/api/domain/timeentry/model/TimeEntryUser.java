package com.github.dynolog.api.domain.timeentry.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class TimeEntryUser {

    @Schema(description = "nano id", example = "V1StGXR8_Z5jdHi6B-myT", required = true)
    private final String id;

    @Schema(example = "Jubileu")
    private final String name;

    TimeEntryUser(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
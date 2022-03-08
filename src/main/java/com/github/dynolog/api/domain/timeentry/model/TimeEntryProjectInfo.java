package com.github.dynolog.api.domain.timeentry.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class TimeEntryProjectInfo {

    @Schema(description = "nano id", example = "V1StGXR8_Z5jdHi6B-myT", required = true)
    private final String id;

    @Schema(example = "Fake Project", required = true)
    private final String name;

    @Schema(example = "#fafafa", required = true)
    private final String color;

    public TimeEntryProjectInfo(String id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }
}

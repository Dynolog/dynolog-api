package com.github.dynolog.api.domain.timeentry.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Setter
public class PartialUpdateTimeEntryProps {
    @Schema(example = "Refatoração no back-end")
    private String description;

    @Schema(example = "2022-02-14T00:32:00.000Z")
    private LocalDateTime start;

    @Schema(example = "2022-02-14T00:48:00.000Z")
    private LocalDateTime stop;

    @Schema(description = "nano id", example = "V1StGXR8_Z5jdHi6B-myT")
    private String projectId;

    public Optional<String> getDescription() {
        return ofNullable(description);
    }

    public Optional<LocalDateTime> getStart() {
        return ofNullable(start);
    }

    public Optional<LocalDateTime> getStop() {
        return ofNullable(stop);
    }

    public Optional<String> getProjectId() {
        return ofNullable(projectId);
    }
}
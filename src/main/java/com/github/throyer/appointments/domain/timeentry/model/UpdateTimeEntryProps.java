package com.github.throyer.appointments.domain.timeentry.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Data
public class UpdateTimeEntryProps {
    private String description = "";

    @NotNull(message = "start is required field")
    private LocalDateTime start;

    @NotNull(message = "stop is required field")
    private LocalDateTime stop;

    private Long projectId;

    public Optional<Long> getProjectId() {
        return ofNullable(projectId);
    }
}

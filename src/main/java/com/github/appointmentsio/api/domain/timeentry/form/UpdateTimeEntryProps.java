package com.github.appointmentsio.api.domain.timeentry.form;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Data
public class UpdateTimeEntryProps {
    private String description = "";

    @NotNull(message = "{timeentry.start.notnull}")
    private LocalDateTime start;

    @NotNull(message = "{timeentry.stop.notnull}")
    private LocalDateTime stop;

    private Long projectId;

    public Optional<Long> getProjectId() {
        return ofNullable(projectId);
    }
}
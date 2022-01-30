package com.github.appointmentsio.api.domain.timeentry.form;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Data
public class CreateTimeEntryProps {
    private String description = "";

    @NotNull(message = "{timeentry.start.notnull}")
    private LocalDateTime start;

    @NotNull(message = "{timeentry.stop.notnull}")
    private LocalDateTime stop;

    @NotNull(message = "{timeentry.user-id.notnull}")
    private Long userId;

    private Long projectId;

    public Optional<Long> getProjectId() {
        return ofNullable(projectId);
    }
}

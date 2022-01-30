package com.github.appointmentsio.api.domain.timeentry.form;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.ofNullable;

@Data
public class UpdateTimeEntryProps {
    private String description = "";

    @NotNull(message = "{timeentry.start.notnull}")
    private LocalDateTime start;

    @NotNull(message = "{timeentry.stop.notnull}")
    private LocalDateTime stop;

    private String projectId;

    public Optional<byte[]> getProjectId() {
        return ofNullable(projectId).map(id -> id.getBytes(UTF_8));
    }
}

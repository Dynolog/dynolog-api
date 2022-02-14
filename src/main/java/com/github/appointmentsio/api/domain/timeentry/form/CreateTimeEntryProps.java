package com.github.appointmentsio.api.domain.timeentry.form;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

@Data
public class CreateTimeEntryProps {
    private String description = "";

    @NotNull(message = "{timeentry.start.notnull}")
    private LocalDateTime start;

    private LocalDateTime stop;

    @NotNull(message = "{timeentry.user-id.notnull}")
    private String userId;

    private String projectId;
}

package com.github.appointmentsio.api.domain.timeentry.form;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CreateTimeEntryProps {
    private String description;

    @NotNull(message = "{timeentry.start.notnull}")
    private LocalDateTime start;

    private LocalDateTime stop;

    @NotNull(message = "{timeentry.user-id.notnull}")
    private String userId;

    private String projectId;
}

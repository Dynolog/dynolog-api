package com.github.appointmentsio.api.domain.timeentry.form;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UpdateTimeEntryProps {
    private String description;

    @NotNull(message = "{timeentry.start.notnull}")
    private LocalDateTime start;

    private LocalDateTime stop;

    private String projectId;
}

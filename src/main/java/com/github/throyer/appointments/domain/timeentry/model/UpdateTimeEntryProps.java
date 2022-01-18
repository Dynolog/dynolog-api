package com.github.throyer.appointments.domain.timeentry.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Data
@NoArgsConstructor
public class UpdateTimeEntryProps {
    private String description;
    private LocalDateTime start;
    private LocalDateTime stop;
    private Long projectId;

    public Optional<Long> getProjectId() {
        return ofNullable(projectId);
    }
}

package com.github.appointmentsio.api.domain.timeentry.model;

import com.github.appointmentsio.api.domain.timeentry.entity.TimeEntry;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimeEntrySummary {
    private final Long id;
    private final String description;
    private final LocalDateTime start;
    private final LocalDateTime stop;

    public TimeEntrySummary(TimeEntry timeEntry) {
        this.id = timeEntry.getId();
        this.description = timeEntry.getDescription();
        this.start = timeEntry.getStart();
        this.stop = timeEntry.getStop();
    }
}

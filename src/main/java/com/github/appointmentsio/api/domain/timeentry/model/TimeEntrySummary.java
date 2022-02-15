package com.github.appointmentsio.api.domain.timeentry.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.appointmentsio.api.domain.timeentry.entity.TimeEntry;
import lombok.Data;

import java.time.LocalDateTime;

import static com.github.appointmentsio.api.utils.Constraints.PATTERNS.DATE_ISO_WITH_TIMEZONE;

@Data
public class TimeEntrySummary {
    private final String id;
    private final String description;

    @JsonFormat(timezone = "UTC", pattern = DATE_ISO_WITH_TIMEZONE)
    private final LocalDateTime start;

    @JsonFormat(timezone = "UTC", pattern = DATE_ISO_WITH_TIMEZONE)
    private final LocalDateTime stop;

    public TimeEntrySummary(TimeEntry timeEntry) {
        this.id = timeEntry.getNanoid();
        this.description = timeEntry.getDescription();
        this.start = timeEntry.getStart();
        this.stop = timeEntry.getStop();
    }
}

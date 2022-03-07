package com.github.dynolog.api.domain.timeentry.model;

import com.github.dynolog.api.domain.timeentry.entity.TimeEntry;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

import static com.github.dynolog.api.utils.Time.format;

public class NonBillableHours {

    @Schema(example = "12:23:40", required = true)
    public final String totalTime;

    @Schema(required = true)
    public final List<TimeEntrySummary> timeEntries;

    public NonBillableHours(List<TimeEntry> timeEntries) {
        this.timeEntries = timeEntries.stream().map(TimeEntrySummary::new).toList();
        this.totalTime = format(TimeEntry.sum(timeEntries));
    }
}

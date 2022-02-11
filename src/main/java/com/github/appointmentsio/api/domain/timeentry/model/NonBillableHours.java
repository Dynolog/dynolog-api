package com.github.appointmentsio.api.domain.timeentry.model;

import com.github.appointmentsio.api.domain.timeentry.entity.TimeEntry;

import java.util.List;

import static com.github.appointmentsio.api.domain.timeentry.entity.TimeEntry.sum;
import static com.github.appointmentsio.api.utils.TimeUtils.millisToTime;

public class NonBillableHours {
    public final String totalTime;
    public final List<TimeEntrySummary> timeEntries;

    public NonBillableHours(List<TimeEntry> timeEntries) {
        this.timeEntries = timeEntries.stream().map(TimeEntrySummary::new).toList();
        this.totalTime = millisToTime(sum(timeEntries));
    }
}

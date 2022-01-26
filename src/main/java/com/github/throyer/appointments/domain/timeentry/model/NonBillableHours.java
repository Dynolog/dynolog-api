package com.github.throyer.appointments.domain.timeentry.model;

import com.github.throyer.appointments.domain.timeentry.entity.TimeEntry;

import java.util.List;

import static com.github.throyer.appointments.domain.timeentry.entity.TimeEntry.sum;
import static com.github.throyer.appointments.utils.TimeUtils.millisToTime;

public class NonBillableHours {
    public final String totalTime;
    public final List<TimeEntrySummary> timeEntries;

    public NonBillableHours(List<TimeEntry> timeEntries) {
        this.timeEntries = timeEntries.stream().map(TimeEntrySummary::new).toList();
        this.totalTime = millisToTime(sum(timeEntries));
    }
}

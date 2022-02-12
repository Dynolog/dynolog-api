package com.github.appointmentsio.api.domain.timeentry.model;

import com.github.appointmentsio.api.domain.shared.model.SimpleEntityRelation;
import com.github.appointmentsio.api.domain.timeentry.entity.TimeEntry;
import lombok.Data;

import java.time.LocalDateTime;

import static com.github.appointmentsio.api.utils.TimeUtils.millisToTime;

@Data
public class TimeEntryInfo {

    private final String id;
    private final String description;
    private final LocalDateTime start;
    private final LocalDateTime stop;
    private final SimpleEntityRelation user;
    private final ProjectInfoInTimeEntryList project;

    public TimeEntryInfo(TimeEntry timeEntry) {
        this.id = timeEntry.getNanoid();
        this.description = timeEntry.getDescription();
        this.start = timeEntry.getStart();
        this.stop = timeEntry.getStop();

        var user = timeEntry.getUser();

        this.user = new SimpleEntityRelation(user.getNanoid(), user.getName());

        this.project = timeEntry
                .getProject()
                .map((project) -> new ProjectInfoInTimeEntryList(project.getNanoid(), project.getName(), project.getColor()))
                .orElse(null);
    }
}


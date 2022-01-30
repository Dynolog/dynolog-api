package com.github.appointmentsio.api.domain.timeentry.model;

import com.github.appointmentsio.api.domain.shared.Identity;
import com.github.appointmentsio.api.domain.shared.SimpleEntityRelation;
import com.github.appointmentsio.api.domain.timeentry.entity.TimeEntry;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimeEntryInfo implements Identity {

    private final Long id;
    private final String description;
    private final LocalDateTime start;
    private final LocalDateTime stop;
    private final SimpleEntityRelation user;
    private final SimpleEntityRelation project;

    public TimeEntryInfo(TimeEntry timeEntry) {
        this.id = timeEntry.getId();
        this.description = timeEntry.getDescription();
        this.start = timeEntry.getStart();
        this.stop = timeEntry.getStop();

        this.user = timeEntry
                .getUser()
                .map((user) -> new SimpleEntityRelation(user.getId(), user.getName()))
                .orElse(null);

        this.project = timeEntry
                .getProject()
                .map((project) -> new SimpleEntityRelation(project.getId(), project.getName()))
                .orElse(null);
    }

    @Override
    public Long getId() {
        return id;
    }
}

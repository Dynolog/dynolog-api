package com.github.appointmentsio.api.domain.timeentry.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.appointmentsio.api.domain.project.model.SimplifiedProject;
import com.github.appointmentsio.api.domain.shared.model.Identity;
import com.github.appointmentsio.api.domain.timeentry.entity.TimeEntry;
import com.github.appointmentsio.api.domain.user.model.SimplifiedUser;
import lombok.Getter;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
public class SimplifiedTimeEntry implements Identity {
    private final Long id;
    private final String description;
    private final LocalDateTime start;
    private final LocalDateTime stop;

    @JsonInclude(NON_NULL)
    private final SimplifiedUser user;

    @JsonInclude(NON_NULL)
    private final SimplifiedProject project;

    public SimplifiedTimeEntry(TimeEntry timeEntry) {
        this.id = timeEntry.getId();
        this.description = timeEntry.getDescription();
        this.start = timeEntry.getStart();
        this.stop = timeEntry.getStop();

        this.user = new SimplifiedUser(timeEntry.getUser());

        this.project = timeEntry
            .getProject()
                .map(SimplifiedProject::new)
            .orElse(null);
    }

    @Override
    public Long getId() {
        return this.id;
    }
}

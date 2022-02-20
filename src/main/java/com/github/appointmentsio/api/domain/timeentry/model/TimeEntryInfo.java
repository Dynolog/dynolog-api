package com.github.appointmentsio.api.domain.timeentry.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.appointmentsio.api.domain.timeentry.entity.TimeEntry;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

import static com.github.appointmentsio.api.utils.Constraints.PATTERNS.DATE_ISO_WITH_TIMEZONE;

@Getter
@Schema(name = "TimeEntry", requiredProperties = {"id", "start", "user"})
public class TimeEntryInfo {

    private final String id;
    private final String description;
    private final TimeEntryUser user;
    private final TimeEntryProjectInfo project;

    @JsonFormat(timezone = "UTC", pattern = DATE_ISO_WITH_TIMEZONE)
    private final LocalDateTime start;

    @JsonFormat(timezone = "UTC", pattern = DATE_ISO_WITH_TIMEZONE)
    private final LocalDateTime stop;

    public TimeEntryInfo(TimeEntry timeEntry) {
        this.id = timeEntry.getNanoid();
        this.description = timeEntry.getDescription();
        this.start = timeEntry.getStart();
        this.stop = timeEntry.getStop();

        var user = timeEntry.getUser();

        this.user = new TimeEntryUser(user.getNanoid(), user.getName());

        this.project = timeEntry
                .getProject()
                .map((project) -> new TimeEntryProjectInfo(project.getNanoid(), project.getName(), project.getColor()))
                .orElse(null);
    }
}


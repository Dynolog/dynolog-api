package com.github.throyer.appointments.domain.timeentry.model;

import com.github.throyer.appointments.domain.shared.Identity;
import com.github.throyer.appointments.domain.timeentry.entity.TimeEntry;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.MILLIS;
import static java.util.Optional.ofNullable;

@Data
@NoArgsConstructor
public class TimeEntryDetails implements Identity {

    private Long id;
    private String description;
    private LocalDateTime start;
    private LocalDateTime stop;

    private TimeEntryRelationship user;
    private TimeEntryRelationship project;

    public TimeEntryDetails(TimeEntry timeEntry) {
        this.id = timeEntry.getId();
        this.description = timeEntry.getDescription();
        this.start = timeEntry.getStart();
        this.stop = timeEntry.getStop();
    }

    public TimeEntryDetails(
        Long id,
        String description,
        LocalDateTime start,
        LocalDateTime stop,
        Long userId,
        String userName,
        Long projectId,
        String projectName
    ) {
        this.id = id;
        this.description = description;
        this.start = start;
        this.stop = stop;

        this.user = ofNullable(userId)
            .map(value -> new TimeEntryRelationship(value, userName))
                .orElse(null);

        this.project = ofNullable(projectId)
            .map(value -> new TimeEntryRelationship(value, projectName))
                .orElse(null);
    }

    public Long totalTimeInMillis() {
        return  MILLIS.between(start, stop);
    }

    @Data
    public static class TimeEntryRelationship {
        private Long id;
        private String name;

        public TimeEntryRelationship(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}

package com.github.throyer.apontamentos.domain.timeentry.dto;

import com.github.throyer.apontamentos.domain.shared.Identity;
import com.github.throyer.apontamentos.domain.timeentry.entity.TimeEntry;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TimeEntryDetails implements Identity {

    private Long id;
    private String description;
    private LocalDateTime start;
    private LocalDateTime stop;

    public TimeEntryDetails(TimeEntry timeEntry) {
        this.id = timeEntry.getId();
        this.description = timeEntry.getDescription();
        this.start = timeEntry.getStart();
        this.stop = timeEntry.getStop();
    }

    public TimeEntryDetails(Long id, String description, LocalDateTime start, LocalDateTime stop) {
        this.id = id;
        this.description = description;
        this.start = start;
        this.stop = stop;
    }
}

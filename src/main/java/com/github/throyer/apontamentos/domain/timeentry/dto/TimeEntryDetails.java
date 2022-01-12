package com.github.throyer.apontamentos.domain.timeentry.dto;

import com.github.throyer.apontamentos.domain.timeentry.entity.TimeEntry;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TimeEntryDetails {

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
}

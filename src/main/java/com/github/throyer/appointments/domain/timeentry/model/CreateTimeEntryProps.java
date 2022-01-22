package com.github.throyer.appointments.domain.timeentry.model;

import java.time.LocalDateTime;
import java.util.Optional;
import static java.util.Optional.ofNullable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateTimeEntryProps {
    private String description;
    private LocalDateTime start;    
    private LocalDateTime stop;    
    private Long userId;    
    private Long projectId;

    public Optional<Long> getUserId() {
        return ofNullable(userId);
    }

    public Optional<Long> getProjectId() {
        return ofNullable(projectId);
    }
}
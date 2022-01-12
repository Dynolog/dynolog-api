package com.github.throyer.apontamentos.domain.timeentry.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateTimeEntryData {

    private String description;
    private LocalDateTime start;
    private LocalDateTime stop;
    private Long userId;
    private Long projectId;
}

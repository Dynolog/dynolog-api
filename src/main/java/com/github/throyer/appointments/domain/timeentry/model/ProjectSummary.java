package com.github.throyer.appointments.domain.timeentry.model;

import com.github.throyer.appointments.domain.project.entity.Project;
import com.github.throyer.appointments.domain.timeentry.entity.TimeEntry;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

import static com.github.throyer.appointments.utils.TimeUtils.millisToTime;

@Data
public class ProjectSummary {
    private Long id;
    private String totalTime;
    private BigDecimal billableHoursAmount;
    private String name;

    public ProjectSummary(
        Project project,
        List<TimeEntry> timeEntries
    ) {
        this.id = project.getId();
        this.name = project.getName();

        var totalMillis = TimeEntry.sum(timeEntries);

        this.totalTime = millisToTime(totalMillis);
        this.billableHoursAmount = project.calcBillableValue(totalMillis);
    }
}

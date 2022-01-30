package com.github.appointmentsio.api.domain.timeentry.model;

import com.github.appointmentsio.api.domain.project.entity.Project;
import com.github.appointmentsio.api.domain.timeentry.entity.TimeEntry;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

import static com.github.appointmentsio.api.utils.TimeUtils.millisToTime;

@Data
public class ProjectSummary {
    private String id;
    private String totalTime;
    private String currency;
    private BigDecimal billableHoursAmount;
    private String name;

    public ProjectSummary(
        Project project,
        List<TimeEntry> timeEntries
    ) {
        this.id = project.getNanoid();
        this.name = project.getName();

        var totalMillis = TimeEntry.sum(timeEntries);

        this.totalTime = millisToTime(totalMillis);
        this.billableHoursAmount = project.calcBillableValue(totalMillis);
        this.currency = project.getCurrency();
    }
}

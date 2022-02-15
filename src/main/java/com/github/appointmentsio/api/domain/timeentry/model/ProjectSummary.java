package com.github.appointmentsio.api.domain.timeentry.model;

import com.github.appointmentsio.api.domain.project.entity.Project;
import com.github.appointmentsio.api.domain.timeentry.entity.TimeEntry;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

import static com.github.appointmentsio.api.utils.TimeUtils.millisToTime;

@Getter
@Schema(requiredProperties = {"id", "totalTime", "currency", "billableHoursAmount", "name"})
public class ProjectSummary {
    private final String id;
    private final String totalTime;
    private final String currency;
    private final String color;
    private final BigDecimal billableHoursAmount;
    private final String name;

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
        this.color = project.getColor();
    }
}

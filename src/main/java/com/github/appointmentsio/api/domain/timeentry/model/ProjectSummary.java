package com.github.appointmentsio.api.domain.timeentry.model;

import com.github.appointmentsio.api.domain.project.entity.Project;
import com.github.appointmentsio.api.domain.timeentry.entity.TimeEntry;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

import static com.github.appointmentsio.api.utils.Time.format;

@Getter
public class ProjectSummary {

    @Schema(description = "nano id", example = "V1StGXR8_Z5jdHi6B-myT", required = true)
    private final String id;

    @Schema(example = "12:23:40", required = true)
    private final String totalTime;

    @Schema(example = "BRL", required = true)
    private final String currency;

    @Schema(example = "#fafafa", required = true)
    private final String color;

    @Schema(example = "29.99", required = true)
    private final BigDecimal billableHoursAmount;

    @Schema(example = "Fake Project", required = true)
    private final String name;

    public ProjectSummary(
        Project project,
        List<TimeEntry> timeEntries
    ) {
        this.id = project.getNanoId();
        this.name = project.getName();

        var totalMillis = TimeEntry.sum(timeEntries);

        this.totalTime = format(totalMillis);
        this.billableHoursAmount = project.calcBillableValue(totalMillis);
        this.currency = project.getCurrency();
        this.color = project.getColor();
    }
}

package com.github.appointmentsio.api.domain.timeentry.model;

import com.github.appointmentsio.api.domain.project.entity.Project;
import com.github.appointmentsio.api.domain.timeentry.entity.TimeEntry;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.github.appointmentsio.api.utils.Time.format;
import static java.util.stream.Collectors.groupingBy;

@Getter
public class Summary {

    @Schema(example = "12:23:40", required = true)
    private final String totalTime;

    @Schema(required = true)
    private final List<Currencies> totalAmountByCurrency = new ArrayList<>();

    @Schema(required = true)
    private NonBillableHours nonBillableHours = new NonBillableHours(List.of());

    private final List<ProjectSummary> projects = new ArrayList<>();

    public Summary(List<TimeEntry> timeEntries) {
        this.totalTime = format(TimeEntry.sum(timeEntries));

        timeEntries.stream()
            .collect(groupingBy(TimeEntry::getProject))
                .forEach(this::addProjectSummary);

        projects.stream()
            .collect(groupingBy(ProjectSummary::getCurrency))
                .forEach(this::addCurrencyAmount);
    }

    private void addProjectSummary(Optional<Project> project, List<TimeEntry> timeEntries) {
        if (project.isPresent()) {
            this.projects.add(new ProjectSummary(project.get(), timeEntries));
        } else {
            this.nonBillableHours = new NonBillableHours(timeEntries);
        }
    }

    private void addCurrencyAmount(String currency, List<ProjectSummary> projects) {
        this.totalAmountByCurrency.add(new Currencies(currency, projects));
    }
}

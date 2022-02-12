package com.github.appointmentsio.api.domain.timeentry.model;

import com.github.appointmentsio.api.domain.project.entity.Project;
import com.github.appointmentsio.api.domain.timeentry.entity.TimeEntry;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.github.appointmentsio.api.utils.TimeUtils.millisToTime;
import static java.util.stream.Collectors.groupingBy;

@Data
public class Summary {
    private final String totalTime;
    private final List<Currencies> totalAmountByCurrency = new ArrayList<>();
    private NonBillableHours nonBillableHours = new NonBillableHours(List.of());

    private final List<ProjectSummary> projects = new ArrayList<>();

    public Summary(List<TimeEntry> timeEntries) {
        this.totalTime = millisToTime(TimeEntry.sum(timeEntries));

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

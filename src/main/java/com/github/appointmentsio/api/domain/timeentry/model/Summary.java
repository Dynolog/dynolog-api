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
    private NonBillableHours nonBillableHours = new NonBillableHours(List.of());

    private final List<ProjectSummary> projects = new ArrayList<>();

    public Summary(List<TimeEntry> timeEntries) {
        this.totalTime = millisToTime(TimeEntry.sum(timeEntries));

        var groupedTimeEntries = timeEntries.stream()
            .collect(groupingBy(TimeEntry::getProject));

        groupedTimeEntries.forEach(this::toBillable);
    }

    private void toBillable(Optional<Project> project, List<TimeEntry> timeEntries) {
        if (project.isPresent()) {
            this.projects.add(new ProjectSummary(project.get(), timeEntries));
        } else {
            this.nonBillableHours = new NonBillableHours(timeEntries);
        }
    }
}

package com.github.appointmentsio.api.domain.project.model;

import com.github.appointmentsio.api.domain.project.entity.Project;
import com.github.appointmentsio.api.domain.shared.SimpleEntityRelation;
import lombok.Data;

import java.math.BigDecimal;

import static java.util.Optional.ofNullable;

@Data
public class ProjectInfo {
    private final Long id;
    private final String name;
    private final BigDecimal hourlyRate;
    private final SimpleEntityRelation user;

    public ProjectInfo(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.hourlyRate = project.getHourlyRate();

        this.user = ofNullable(project.getUser())
            .map((user) -> new SimpleEntityRelation(user.getId(), user.getName()))
                .orElse(null);
    }
}

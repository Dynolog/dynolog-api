package com.github.throyer.appointments.domain.project.model;

import com.github.throyer.appointments.domain.project.entity.Project;
import com.github.throyer.appointments.domain.shared.SimpleEntityRelation;
import lombok.Data;

import java.math.BigDecimal;

import static java.util.Optional.ofNullable;

@Data
public class ProjectInfo {
    private final Long id;
    private final String name;
    private final BigDecimal hourlyHate;
    private final SimpleEntityRelation user;

    public ProjectInfo(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.hourlyHate = project.getHourlyHate();

        this.user = ofNullable(project.getUser())
            .map((user) -> new SimpleEntityRelation(user.getId(), user.getName()))
                .orElse(null);
    }
}

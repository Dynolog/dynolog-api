package com.github.appointmentsio.api.domain.project.model;

import com.github.appointmentsio.api.domain.project.entity.Project;
import com.github.appointmentsio.api.domain.shared.model.SimpleEntityRelation;
import lombok.Data;

import java.math.BigDecimal;

import static java.util.Optional.ofNullable;

@Data
public class ProjectInfo {
    private final String id;
    private final String name;
    private final BigDecimal hourlyRate;
    private final SimpleEntityRelation user;

    public ProjectInfo(Project project) {
        this.id = project.getNanoid();
        this.name = project.getName();
        this.hourlyRate = project.getHourlyRate();

        this.user = ofNullable(project.getUser())
            .map((user) -> new SimpleEntityRelation(user.getNanoid(), user.getName()))
                .orElse(null);
    }
}

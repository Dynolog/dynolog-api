package com.github.throyer.appointments.domain.project.model;

import com.github.throyer.appointments.domain.project.entity.Project;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProjectDetails {
    private Long id;
    private String name;
    private BigDecimal hourlyHate;

    public ProjectDetails(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.hourlyHate = project.getHourlyHate();
    }
}

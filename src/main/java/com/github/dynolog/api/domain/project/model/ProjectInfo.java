package com.github.dynolog.api.domain.project.model;

import com.github.dynolog.api.domain.project.entity.Project;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.math.BigDecimal;

import static java.util.Optional.ofNullable;

@Getter
@Schema(name = "Project", requiredProperties = {"id", "name", "hourlyRate", "user"})
public class ProjectInfo {
    private final String id;
    private final String name;
    private final String color;
    private final BigDecimal hourlyRate;
    private final ProjectUser user;

    public ProjectInfo(Project project) {
        this.id = project.getNanoId();
        this.name = project.getName();
        this.color = project.getColor();
        this.hourlyRate = project.getHourlyRate();

        this.user = ofNullable(project.getUser())
            .map((user) -> new ProjectUser(user.getNanoId(), user.getName()))
                .orElse(null);
    }
}

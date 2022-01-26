package com.github.throyer.appointments.domain.project.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.throyer.appointments.domain.project.entity.Project;
import com.github.throyer.appointments.domain.user.model.SimplifiedUser;
import lombok.Getter;

import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static java.util.Optional.ofNullable;

@Getter
public class SimplifiedProject {
    private final Long id;
    private final String name;
    private final BigDecimal hourlyHate;

    @JsonInclude(NON_NULL)
    private final SimplifiedUser user;

    public SimplifiedProject(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.hourlyHate = project.getHourlyHate();

        this.user = ofNullable(project.getUser())
            .map(SimplifiedUser::new)
                .orElse(null);
    }
}

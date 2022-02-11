package com.github.appointmentsio.api.domain.project.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.appointmentsio.api.domain.project.entity.Project;
import com.github.appointmentsio.api.domain.user.model.SimplifiedUser;
import lombok.Getter;

import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static java.util.Optional.ofNullable;

@Getter
public class SimplifiedProject {
    private final Long id;
    private final String name;
    private final BigDecimal hourlyRate;
    private final SimplifiedUser user;

    public SimplifiedProject(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.hourlyRate = project.getHourlyRate();

        this.user = ofNullable(project.getUser())
            .map(SimplifiedUser::new)
                .orElse(null);
    }
}

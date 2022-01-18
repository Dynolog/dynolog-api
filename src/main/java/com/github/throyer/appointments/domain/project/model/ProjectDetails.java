package com.github.throyer.appointments.domain.project.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectDetails {
    private Long id;
    private String name;

    public ProjectDetails(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}

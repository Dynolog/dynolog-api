package com.github.throyer.appointments.domain.project.repository;

public class Queries {
    public static final String FIND_PROJECT_BY_ID_FETCH_USER = """
        select project from Project project
        left join fetch project.user
        where project.id = :id
    """;
}

package com.github.appointmentsio.api.domain.project.repository;

public class Queries {
    public static final String FIND_PROJECT_BY_ID_FETCH_USER = """
                select project from Project project
                left join fetch project.user
                where project.id = :id
            """;

    public static final String FIND_ALL_FETCH_USER = """
                select
                    new com.github.appointmentsio.api.domain.project.entity.Project(
                        project.id,
                        project.name,
                        project.hourlyHate,
                        user.id,
                        user.name
                    )
                from Project project
                left join project.user user
                where project.user.id = :user_id
            """;
}

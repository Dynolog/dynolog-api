package com.github.appointmentsio.api.domain.project.repository;

public class Queries {
    private Queries() { }

    public static final String FIND_PROJECT_BY_NANO_ID_FETCH_USER = """
                select project from Project project
                left join fetch project.user
                where project.nanoId = :project_nano_id
            """;

    public static final String FIND_ALL_BY_USER_NANO_ID_FETCH_USER = """
                select project from Project project
                left join fetch project.user user
                where project.user.nanoId = :user_nano_id
            """;
}

package com.github.appointmentsio.api.domain.project.repository;

public class Queries {
    private Queries() {
    }

    public static final String FIND_PROJECT_BY_ID_FETCH_USER = """
                select project from Project project
                left join fetch project.user
                where project.id = :id
            """;

    public static final String FIND_PROJECT_BY_NANOID_FETCH_USER = """
                select project from Project project
                left join fetch project.user
                where project.nanoid = :nanoid
            """;

    public static final String FIND_ALL_FETCH_USER = """
                select project from Project project
                left join fetch project.user user
                where project.user.id = :user_id
            """;
}

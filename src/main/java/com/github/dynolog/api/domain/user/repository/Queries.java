package com.github.dynolog.api.domain.user.repository;

public class Queries {
    private Queries() { }
    public static final String FIND_USER_FETCH_ROLES_QUERY = """
        with user_roles as (
            select
                    ur.user_id, string_agg(r.initials, ',') roles
            from "role" r
                    left join user_role ur on r.id = ur.role_id
            group by ur.user_id
        )
        select
            u.id,
            u.nano_id,
            u."name",
            u.email,
            u.password,
            u.timezone,
            u.date_format,
            u.time_format,
            urs.roles
        from
            "user" u
        left join user_roles as urs on urs.user_id = u.id
    """;
}

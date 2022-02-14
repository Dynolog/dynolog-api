package com.github.appointmentsio.api.domain.timeentry.repository;

public final class Queries {
    private Queries() {
    }

    public static final String SELECT = """
                new com.github.appointmentsio.api.domain.timeentry.entity.TimeEntry(
                    time_entry.id,
                    time_entry.nanoid,
                    time_entry.description,
                    time_entry.start,
                    time_entry.stop,
                    user.id,
                    user.nanoid,
                    user.name,
                    project.id,
                    project.nanoid,
                    project.name,
                    project.color,
                    project.hourlyRate,
                    project.currency
                )
            """;

    public static final String FIND_TIME_ENTRIES_BY_USER_ID_FETCH_USER_AND_PROJECT = """
                SELECT
            """ + SELECT + """
                FROM TimeEntry AS time_entry
                LEFT JOIN time_entry.user AS user
                LEFT JOIN time_entry.project AS project
                WHERE time_entry.start >= :start_date AND time_entry.stop <= :end_date AND user.nanoid = :user_nanoid
                ORDER BY time_entry.start DESC
            """;

    public static final String FIND_TIME_ENTRIES_BY_USER_NANOID_WHERE_STOP_NULL_FETCH_USER_AND_PROJECT = """
                select
                    t.id as "time_entry_id",
                    t.nanoid as "time_entry_nanoid",
                    t.description as "time_entry_description",
                    t.start as "time_entry_start",
                    t.stop as "time_entry_stop",
                    u.id as "user_id",
                    u.nanoid as "user_nanoid",
                    u.name as "user_name",
                    p.id as "project_id",
                    p.nanoid as "project_nanoid",
                    p.name as "project_name",
                    p.color as "project_color",
                    p.hourly_rate as "project_hourly_rate",
                    p.currency as "project_currency"
                from
                    "time_entry" t
                left outer join "user" u on t."user_id" = u."id"
                left outer join "project" p on t."project_id" = p."id"
                where
                    (t."stop" is null)
                    and u."nanoid" = :user_nano_id
                order by
                    t."start" desc
                limit 1
            """;

    public static final String FIND_TIME_ENTRIES_BY_USER_ID_BETWEEN_START_AND_END_DATE = """
            SELECT
                """ + SELECT + """
                FROM TimeEntry AS time_entry
                LEFT JOIN time_entry.user AS user
                LEFT JOIN time_entry.project AS project
                WHERE time_entry.start >= :start_date AND time_entry.stop <= :end_date AND user.nanoid = :user_nanoid
                ORDER BY time_entry.start DESC
            """;

    public static final String FIND_TIME_ENTRY_BY_ID = """
            SELECT
                """ + SELECT + """
                FROM TimeEntry AS time_entry
                LEFT JOIN time_entry.user AS user
                LEFT JOIN time_entry.project AS project
                WHERE time_entry.id = :id
            """;

    public static final String FIND_BY_NANOID = """
                select time_entry from TimeEntry time_entry
                left join fetch time_entry.user
                left join fetch time_entry.project
                where time_entry.nanoid = :nanoid
            """;
}

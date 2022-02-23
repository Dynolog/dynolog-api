package com.github.appointmentsio.api.domain.timeentry.repository;

public final class Queries {
    private Queries() { }

    public static final String SELECT = """
                new com.github.appointmentsio.api.domain.timeentry.entity.TimeEntry(
                    time_entry.id,
                    time_entry.nanoId,
                    time_entry.description,
                    time_entry.start,
                    time_entry.stop,
                    user.id,
                    user.nanoId,
                    user.name,
                    project.id,
                    project.nanoId,
                    project.name,
                    project.color,
                    project.hourlyRate,
                    project.currency
                )
            """;

    public static final String FIND_TIME_ENTRIES_BY_USER_NANO_ID_FETCH_USER_AND_PROJECT = """
                SELECT
            """ + SELECT + """
                FROM TimeEntry AS time_entry
                LEFT JOIN time_entry.user AS user
                LEFT JOIN time_entry.project AS project
                WHERE time_entry.start >= :start_date AND time_entry.stop <= :end_date AND user.nanoId = :user_nano_id
                ORDER BY time_entry.start DESC
            """;

    public static final String FIND_TIME_ENTRIES_BY_USER_NANO_ID_WHERE_STOP_NULL_FETCH_USER_AND_PROJECT = """
                select
                    t.id as "time_entry_id",
                    t.nano_id as "time_entry_nano_id",
                    t.description as "time_entry_description",
                    t.start as "time_entry_start",
                    t.stop as "time_entry_stop",
                    u.id as "user_id",
                    u.nano_id as "user_nano_id",
                    u.name as "user_name",
                    p.id as "project_id",
                    p.nano_id as "project_nano_id",
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
                    and u."nano_id" = :user_nano_id
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
                WHERE time_entry.start >= :start_date AND time_entry.stop <= :end_date AND user.nanoId = :user_nano_id
                ORDER BY time_entry.start DESC
            """;

    public static final String FIND_TIME_ENTRY_BY_ID = """
            SELECT
                """ + SELECT + """
                FROM TimeEntry AS time_entry
                LEFT JOIN time_entry.user AS user
                LEFT JOIN time_entry.project AS project
                WHERE time_entry.id = :time_entry_id
            """;

    public static final String FIND_BY_NANO_ID = """
                select time_entry from TimeEntry time_entry
                left join fetch time_entry.user
                left join fetch time_entry.project
                where time_entry.nanoId = :time_entry_nano_id
            """;
}

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
                WHERE time_entry.start >= :start_date AND time_entry.stop <= :end_date AND user.id = :user_id
                ORDER BY time_entry.start DESC
            """;

    public static final String FIND_TIME_ENTRIES_BY_USER_ID_BETWEEN_START_AND_END_DATE = """
            SELECT
                """ + SELECT + """
                FROM TimeEntry AS time_entry
                LEFT JOIN time_entry.user AS user
                LEFT JOIN time_entry.project AS project
                WHERE time_entry.start >= :start_date AND time_entry.stop <= :end_date AND user.id = :user_id
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

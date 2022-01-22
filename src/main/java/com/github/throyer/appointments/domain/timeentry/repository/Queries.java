package com.github.throyer.appointments.domain.timeentry.repository;

public final class Queries {
    private Queries() {
    }

    public static final String FIND_TIME_ENTRIES_BY_USER_ID_FETCH_USER_AND_PROJECT = """
                SELECT
                    new com.github.throyer.appointments.domain.timeentry.model.TimeEntryDetails(
                        time_entry.id,
                        time_entry.description,
                        time_entry.start,
                        time_entry.stop,
                        user.id,
                        user.name,
                        project.id,
                        project.name
                    )
                FROM TimeEntry AS time_entry
                LEFT JOIN time_entry.user AS user
                LEFT JOIN time_entry.project AS project
                WHERE time_entry.start >= :start_date AND time_entry.stop <= :end_date AND user.id = :user_id
                ORDER BY time_entry.start DESC
            """;

    public static final String FIND_ALL_TIME_ENTRIES_FETCH_USER_AND_PROJECT = """
                SELECT
                    new com.github.throyer.appointments.domain.timeentry.model.TimeEntryDetails(
                        time_entry.id,
                        time_entry.description,
                        time_entry.start,
                        time_entry.stop,
                        user.id,
                        user.name,
                        project.id,
                        project.name
                    )
                FROM TimeEntry AS time_entry
                LEFT JOIN time_entry.user AS user
                LEFT JOIN time_entry.project AS project
                WHERE time_entry.start >= :start_date AND time_entry.stop <= :end_date
                ORDER BY time_entry.start DESC
            """;

    public static final String FIND_TIME_ENTRIES_BY_USER_ID_BETWEEN_START_AND_END_DATE = """
                SELECT
                    new com.github.throyer.appointments.domain.timeentry.model.TimeEntryDetails(
                        time_entry.id,
                        time_entry.description,
                        time_entry.start,
                        time_entry.stop,
                        user.id,
                        user.name,
                        project.id,
                        project.name
                    )
                FROM TimeEntry AS time_entry
                LEFT JOIN time_entry.user AS user
                LEFT JOIN time_entry.project AS project
                WHERE time_entry.start >= :start_date AND time_entry.stop <= :end_date AND user.id = :user_id
                ORDER BY time_entry.start DESC
            """;

    public static final String FIND_TIME_ENTRY_BY_ID = """
                SELECT
                    new com.github.throyer.appointments.domain.timeentry.model.TimeEntryDetails(
                        time_entry.id,
                        time_entry.description,
                        time_entry.start,
                        time_entry.stop,
                        user.id,
                        user.name,
                        project.id,
                        project.name
                    )
                FROM TimeEntry AS time_entry
                LEFT JOIN time_entry.user AS user
                LEFT JOIN time_entry.project AS project
                WHERE time_entry.id = :id
            """;
}

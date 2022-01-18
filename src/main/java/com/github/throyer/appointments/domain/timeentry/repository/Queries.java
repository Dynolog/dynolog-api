package com.github.throyer.appointments.domain.timeentry.repository;

public final class Queries {
    private Queries() { }

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
        WHERE user.id = :id
        ORDER BY time_entry.stop
    """;

    public static final String FIND_TIME_ENTRIES_ALL_USER_ID_FETCH_USER_AND_PROJECT = """
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
        ORDER BY time_entry.stop
    """;
}

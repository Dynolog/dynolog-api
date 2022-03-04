package com.github.appointmentsio.api.domain.timeentry.repository.springdata;

public final class Queries {
    private Queries() { }

    public static final String FIND_BY_NANO_ID = """
        select time_entry from TimeEntry time_entry
        left join fetch time_entry.user
        left join fetch time_entry.project
        where time_entry.nanoId = :time_entry_nano_id
    """;
}

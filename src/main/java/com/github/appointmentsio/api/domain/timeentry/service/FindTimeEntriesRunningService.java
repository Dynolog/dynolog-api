package com.github.appointmentsio.api.domain.timeentry.service;

import static com.github.appointmentsio.api.utils.Response.notFound;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.ofNullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

import com.github.appointmentsio.api.domain.timeentry.entity.TimeEntry;
import com.github.appointmentsio.api.domain.timeentry.repository.TimeEntryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FindTimeEntriesRunningService {

    private final TimeEntryRepository repository;

    @Autowired
    public FindTimeEntriesRunningService(TimeEntryRepository repository) {
        this.repository = repository;
    }

    public TimeEntry find(String id) {
        var tuple = repository.findByUserNanoIdWhereStopIdNullFetchUserAndProject(id.getBytes(UTF_8))
                .orElseThrow(() -> notFound("Time entry not found"));

        return new TimeEntry(
                tuple.get("time_entry_id", BigInteger.class).longValue(),
                tuple.get("time_entry_nano_id", byte[].class),
                tuple.get("time_entry_description", String.class),
                ofNullable(tuple.get("time_entry_start", Timestamp.class)).map(Timestamp::toLocalDateTime).orElse(null),
                ofNullable(tuple.get("time_entry_stop", Timestamp.class)).map(Timestamp::toLocalDateTime).orElse(null),
                tuple.get("user_id", BigInteger.class).longValue(),
                tuple.get("user_nano_id", byte[].class),
                tuple.get("user_name", String.class),
                ofNullable(tuple.get("project_id", BigInteger.class)).map(BigInteger::longValue).orElse(null),
                tuple.get("project_nano_id", byte[].class),
                tuple.get("project_name", String.class),
                tuple.get("project_color", String.class),
                tuple.get("project_hourly_rate", BigDecimal.class),
                tuple.get("project_currency", String.class)
        );
    }
}

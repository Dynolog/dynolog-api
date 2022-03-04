package com.github.appointmentsio.api.domain.timeentry.service;

import static com.github.appointmentsio.api.utils.Response.notFound;
import static java.nio.charset.StandardCharsets.UTF_8;

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
        return repository.findRunningByUserNanoId(id)
                .orElseThrow(() -> notFound("Time entry not found"));
    }
}

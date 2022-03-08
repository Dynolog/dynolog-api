package com.github.dynolog.api.domain.timeentry.service;

import static com.github.dynolog.api.utils.Constants.MESSAGES.NO_TIMEENTRY_RUNNING;
import static com.github.dynolog.api.utils.Messages.message;
import static com.github.dynolog.api.utils.Response.noContent;
import static com.github.dynolog.api.utils.Response.notFound;

import com.github.dynolog.api.domain.timeentry.entity.TimeEntry;
import com.github.dynolog.api.domain.timeentry.repository.TimeEntryRepository;

import com.github.dynolog.api.utils.Response;
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
                .orElseThrow(() -> Response.noContent(message(NO_TIMEENTRY_RUNNING)));
    }
}

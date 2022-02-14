package com.github.appointmentsio.api.domain.timeentry.service;

import com.github.appointmentsio.api.domain.timeentry.model.TimeEntryInfo;
import com.github.appointmentsio.api.domain.timeentry.repository.TimeEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.github.appointmentsio.api.utils.Response.notFound;
import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class FindTimeEntriesRunningService {

    private final TimeEntryRepository repository;

    @Autowired
    public FindTimeEntriesRunningService(TimeEntryRepository repository) {
        this.repository = repository;
    }

    public TimeEntryInfo find(String userNanoid) {
        var entry = repository.findByUserNanoidWhereStopIdNullFetchUserAndProject(PageRequest.of(0, 1), userNanoid.getBytes(UTF_8))
                .getContent()
                .stream()
                .findFirst()
                .orElseThrow(() -> notFound("Time entry not found"));
        return new TimeEntryInfo(entry);
    }
}

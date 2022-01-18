package com.github.throyer.appointments.domain.timeentry.service;

import com.github.throyer.appointments.domain.timeentry.entity.TimeEntry;
import com.github.throyer.appointments.domain.timeentry.model.TimeEntryDetails;
import com.github.throyer.appointments.domain.timeentry.model.UpdateTimeEntryProps;
import com.github.throyer.appointments.domain.timeentry.repository.TimeEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.github.throyer.appointments.utils.Response.notFound;

@Service
public class UpdateTimeEntryService {

    @Autowired
    private TimeEntryRepository repository;

    public TimeEntryDetails update(Long id, UpdateTimeEntryProps props) {
        var entry = repository.findById(id)
                .orElseThrow(() -> notFound("time entry not found"));

        entry.update(props);

        return new TimeEntryDetails(repository.save(entry));
    }
}

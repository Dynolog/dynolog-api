package com.github.throyer.appointments.domain.timeentry.service;

import com.github.throyer.appointments.domain.timeentry.model.CreateTimeEntryProps;
import com.github.throyer.appointments.domain.timeentry.model.TimeEntryDetails;
import com.github.throyer.appointments.domain.timeentry.entity.TimeEntry;
import com.github.throyer.appointments.domain.timeentry.repository.TimeEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateTimeEntryService {
    
    @Autowired
    private TimeEntryRepository repository;
    
    public TimeEntryDetails create(CreateTimeEntryProps props) {
        var created = repository.save(new TimeEntry(props));
        return repository.findByIdFetchUserAndProject(created.getId());
    }
}

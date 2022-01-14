package com.github.throyer.appointments.domain.timeentry.service;

import com.github.throyer.appointments.domain.shared.pagination.Page;
import static com.github.throyer.appointments.domain.shared.pagination.Page.of;
import com.github.throyer.appointments.domain.shared.pagination.Pagination;
import com.github.throyer.appointments.domain.timeentry.dto.TimeEntryDetails;
import com.github.throyer.appointments.domain.timeentry.repository.TimeEntryRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class FindTimeEntryService {

    @Autowired
    public TimeEntryRepository repository;

    public Page<TimeEntryDetails> findAll(Pagination pagination, Optional<Long> userId) {
        if (userId.isPresent()) {
            var page = repository.findByUserId(pagination.build(Sort.by("id")), userId.get());
            return of(page);
        }
        
        var page = repository.findAll(pagination.build(Sort.by("id")));
        return of(page.map(TimeEntryDetails::new));
    }
}

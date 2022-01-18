package com.github.throyer.appointments.domain.timeentry.service;

import com.github.throyer.appointments.domain.pagination.Page;
import static com.github.throyer.appointments.domain.pagination.Page.of;
import com.github.throyer.appointments.domain.pagination.Pagination;
import com.github.throyer.appointments.domain.timeentry.model.TimeEntryDetails;
import com.github.throyer.appointments.domain.timeentry.repository.TimeEntryRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class FindTimeEntryService {

    @Autowired
    public TimeEntryRepository repository;

    public Page<TimeEntryDetails> findAll(
        Optional<Integer> pageNumber,
        Optional<Integer> pageSize,
        Optional<Long> userId
    ) {
        var pageable = Pagination.of(pageNumber, pageSize);
        if (userId.isPresent()) {
            var page = repository.findByUserIdFetchUserAndProject(pageable, userId.get());
            return of(page);
        }
        
        var page = repository.findAllFetchUserAndProject(pageable);
        return of(page);
    }
}

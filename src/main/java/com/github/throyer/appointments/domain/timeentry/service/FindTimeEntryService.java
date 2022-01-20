package com.github.throyer.appointments.domain.timeentry.service;

import com.github.throyer.appointments.domain.pagination.Page;
import com.github.throyer.appointments.domain.pagination.Pagination;
import com.github.throyer.appointments.domain.timeentry.model.TimeEntryDetails;
import com.github.throyer.appointments.domain.timeentry.repository.TimeEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.github.throyer.appointments.domain.pagination.Page.of;
import static com.github.throyer.appointments.domain.session.service.SessionService.authorized;
import static com.github.throyer.appointments.utils.Response.unauthorized;

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
        var page = authorized()
            .map(authorized -> userId
                .filter(authorized::canRead)
                    .map(id -> repository.findAllByUserIdFetchUserAndProject(pageable, id))
                        .orElseThrow(() -> unauthorized("Unauthorized")))
            .orElseThrow(() -> unauthorized("Unauthorized"));
        return of(page);
    }
}

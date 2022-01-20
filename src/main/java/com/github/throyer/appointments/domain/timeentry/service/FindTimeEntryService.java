package com.github.throyer.appointments.domain.timeentry.service;

import com.github.throyer.appointments.domain.pagination.Page;
import com.github.throyer.appointments.domain.pagination.Pagination;
import com.github.throyer.appointments.domain.timeentry.model.TimeEntryDetails;
import com.github.throyer.appointments.domain.timeentry.repository.TimeEntryRepository;
import com.github.throyer.appointments.errors.Error;
import com.github.throyer.appointments.errors.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;

import static com.github.throyer.appointments.domain.pagination.Page.of;
import static com.github.throyer.appointments.domain.session.service.SessionService.authorized;
import static com.github.throyer.appointments.utils.Response.unauthorized;
import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.*;
import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@Service
public class FindTimeEntryService {

    @Autowired
    public TimeEntryRepository repository;

    public Page<TimeEntryDetails> findAll(
        Optional<LocalDateTime> optionalStart,
        Optional<LocalDateTime> optionalEnd,
        Optional<Integer> pageNumber,
        Optional<Integer> pageSize,
        Optional<Long> userId
    ) {
        var start = optionalStart.orElse(now().with(firstDayOfMonth()));
        var end = optionalEnd.orElse(now().with(lastDayOfMonth()));

        var errors = new ArrayList<Error>();

        if (start.isAfter(end) || end.isBefore(start)) {
            errors.add(new Error("start_date or end_date", "start_date or end_date interval invalid"));
        }

        if (MONTHS.between(start, end) > 1) {
            errors.add(new Error("interval", "The interval cannot be longer than 6 months"));
        }

        if (!errors.isEmpty()) {
            throw new BadRequestException(errors);
        }

        var pageable = Pagination.of(pageNumber, pageSize);

        var authorized = authorized()
            .orElseThrow(() -> unauthorized("Unauthorized"));

        if (userId.isPresent()) {
            if (authorized.canRead(userId.get())) {
                return of(repository.findAllByUserIdFetchUserAndProject(pageable, start, end, userId.get()));
            }
        }

        if (authorized.isAdmin()) {
            return of(repository.findAllFetchUserAndProject(pageable, start, end));
        }

        throw unauthorized("Unauthorized");
    }
}

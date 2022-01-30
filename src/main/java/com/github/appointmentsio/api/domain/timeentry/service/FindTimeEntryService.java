package com.github.appointmentsio.api.domain.timeentry.service;

import com.github.appointmentsio.api.domain.pagination.Page;
import com.github.appointmentsio.api.domain.pagination.Pagination;
import com.github.appointmentsio.api.domain.timeentry.model.TimeEntryInfo;
import com.github.appointmentsio.api.domain.timeentry.repository.TimeEntryRepository;
import com.github.appointmentsio.api.errors.Error;
import com.github.appointmentsio.api.errors.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.github.appointmentsio.api.domain.pagination.Page.of;
import static com.github.appointmentsio.api.domain.session.service.SessionService.authorizedOrThrow;
import static com.github.appointmentsio.api.utils.Constraints.MESSAGES.DATES_INTERVAL_CANNOT_LONGER_THAN_MONTHS;
import static com.github.appointmentsio.api.utils.Constraints.MESSAGES.SEARCH_DATE_INTERVAL_INVALID;
import static com.github.appointmentsio.api.utils.Messages.message;
import static com.github.appointmentsio.api.utils.Response.unauthorized;
import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@Service
public class FindTimeEntryService {

    @Autowired
    public TimeEntryRepository repository;

    public Page<TimeEntryInfo> findAll(
        Optional<LocalDateTime> optionalStart,
        Optional<LocalDateTime> optionalEnd,
        Optional<Integer> pageNumber,
        Optional<Integer> pageSize,
        Long userId
    ) {
        var authorized = authorizedOrThrow();

        if (!authorized.canRead(userId)) {
            throw unauthorized("Not authorized to list this user's time entries");
        }

        var start = optionalStart.orElse(now().with(firstDayOfMonth()));
        var end = optionalEnd.orElse(now().with(lastDayOfMonth()));

        var exception = new BadRequestException();

        if (start.isAfter(end) || end.isBefore(start)) {
            exception.add(new Error("start_date or end_date", message(SEARCH_DATE_INTERVAL_INVALID)));
        }

        if (MONTHS.between(start, end) > 6) {
            exception.add(new Error("start_date or end_date", message(DATES_INTERVAL_CANNOT_LONGER_THAN_MONTHS, 6)));
        }

        if (exception.hasError()) {
            throw exception;
        }

        var pageable = Pagination.of(pageNumber, pageSize);

        var page = repository.findAllByUserIdFetchUserAndProject(pageable, start, end, userId).map(TimeEntryInfo::new);

        return of(page);
    }
}

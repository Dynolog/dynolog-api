package com.github.appointmentsio.api.domain.timeentry.service;

import com.github.appointmentsio.api.domain.pagination.Page;
import com.github.appointmentsio.api.domain.pagination.Pagination;
import com.github.appointmentsio.api.domain.timeentry.model.TimeEntryInfo;
import com.github.appointmentsio.api.domain.timeentry.repository.TimeEntryRepository;
import com.github.appointmentsio.api.domain.user.repository.UserRepository;
import com.github.appointmentsio.api.errors.model.FieldError;
import com.github.appointmentsio.api.errors.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.github.appointmentsio.api.domain.pagination.Page.of;
import static com.github.appointmentsio.api.domain.session.service.SessionService.authorizedOrThrow;
import static com.github.appointmentsio.api.utils.Constraints.MESSAGES.*;
import static com.github.appointmentsio.api.utils.Messages.message;
import static com.github.appointmentsio.api.utils.Response.unauthorized;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@Service
public class FindTimeEntryService {

    @Autowired
    public FindTimeEntryService(
            TimeEntryRepository timeEntryRepository,
            UserRepository userRepository
    ) {
        this.timeEntryRepository = timeEntryRepository;
        this.userRepository = userRepository;
    }

    public final TimeEntryRepository timeEntryRepository;
    public final UserRepository userRepository;

    public Page<TimeEntryInfo> findAll (
        Optional<LocalDateTime> optionalStart,
        Optional<LocalDateTime> optionalEnd,
        Optional<Integer> pageNumber,
        Optional<Integer> pageSize,
        String userNanoid
    ) {
        var authorized = authorizedOrThrow();

        if (!authorized.canRead(userNanoid)) {
            throw unauthorized(message(NOT_AUTHORIZED_TO_LIST, "'time entries"));
        }

        var start = optionalStart.orElse(now().with(firstDayOfMonth()));
        var end = optionalEnd.orElse(now().with(lastDayOfMonth()));

        var exception = new BadRequestException();

        if (start.isAfter(end) || end.isBefore(start)) {
            exception.add(new FieldError("start_date or end_date", message(SEARCH_DATE_INTERVAL_INVALID)));
        }

        if (MONTHS.between(start, end) > 6) {
            exception.add(new FieldError("start_date or end_date", message(DATES_INTERVAL_CANNOT_LONGER_THAN_MONTHS, 6)));
        }

        if (exception.hasError()) {
            throw exception;
        }

        var pageable = Pagination.of(pageNumber, pageSize);

        var page = timeEntryRepository.findAllByUserIdFetchUserAndProject(pageable, start, end, userNanoid.getBytes(UTF_8)).map(TimeEntryInfo::new);

        return of(page);
    }
}

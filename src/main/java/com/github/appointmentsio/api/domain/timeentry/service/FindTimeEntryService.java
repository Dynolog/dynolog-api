package com.github.appointmentsio.api.domain.timeentry.service;

import static com.github.appointmentsio.api.domain.pagination.Page.of;
import static com.github.appointmentsio.api.utils.Constants.MESSAGES.DATES_INTERVAL_CANNOT_LONGER_THAN_MONTHS;
import static com.github.appointmentsio.api.utils.Constants.MESSAGES.SEARCH_DATE_INTERVAL_INVALID;
import static com.github.appointmentsio.api.utils.Messages.message;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

import java.time.LocalDateTime;
import java.util.Optional;

import com.github.appointmentsio.api.domain.pagination.Page;
import com.github.appointmentsio.api.domain.pagination.Pagination;
import com.github.appointmentsio.api.domain.timeentry.entity.TimeEntry;
import com.github.appointmentsio.api.domain.timeentry.repository.TimeEntryRepository;
import com.github.appointmentsio.api.domain.user.repository.UserRepository;
import com.github.appointmentsio.api.errors.exception.BadRequestException;
import com.github.appointmentsio.api.errors.model.FieldError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Page<TimeEntry> findAll (
        Optional<LocalDateTime> optionalStart,
        Optional<LocalDateTime> optionalEnd,
        Optional<Integer> pageNumber,
        Optional<Integer> pageSize,
        String userNanoId
    ) {
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

        var page = timeEntryRepository.findAllByUserIdFetchUserAndProject(pageable, start, end, userNanoId.getBytes(UTF_8));

        return of(page);
    }
}

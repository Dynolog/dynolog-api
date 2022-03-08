package com.github.dynolog.api.domain.timeentry.service;

import com.github.dynolog.api.domain.pagination.Page;
import com.github.dynolog.api.domain.pagination.Pagination;
import com.github.dynolog.api.domain.timeentry.entity.TimeEntry;
import com.github.dynolog.api.domain.timeentry.repository.TimeEntryRepository;
import com.github.dynolog.api.domain.user.repository.UserRepository;
import com.github.dynolog.api.errors.exception.BadRequestException;
import com.github.dynolog.api.errors.model.FieldError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.github.dynolog.api.utils.Constants.MESSAGES.DATES_INTERVAL_CANNOT_LONGER_THAN_MONTHS;
import static com.github.dynolog.api.utils.Constants.MESSAGES.SEARCH_DATE_INTERVAL_INVALID;
import static com.github.dynolog.api.utils.Messages.message;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.util.stream.Stream.of;

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
        if (of(optionalStart, optionalEnd).allMatch(Optional::isPresent)) {
            var exception = new BadRequestException();

            var start = optionalStart.get();
            var end = optionalEnd.get();

            if (start.isAfter(end) || end.isBefore(start)) {
                exception.add(new FieldError("start_date or end_date", message(SEARCH_DATE_INTERVAL_INVALID)));
            }

            if (MONTHS.between(start, end) > 6) {
                exception.add(new FieldError("start_date or end_date", message(DATES_INTERVAL_CANNOT_LONGER_THAN_MONTHS, 6)));
            }

            if (exception.hasError()) {
                throw exception;
            }
        }

        var pageable = Pagination.of(pageNumber, pageSize);

        return timeEntryRepository.findAll(pageable, optionalStart, optionalEnd, userNanoId);
    }
}

package com.github.appointmentsio.api.domain.timeentry.service;

import com.github.appointmentsio.api.domain.timeentry.model.Summary;
import com.github.appointmentsio.api.domain.timeentry.repository.TimeEntryRepository;
import com.github.appointmentsio.api.errors.Error;
import com.github.appointmentsio.api.errors.exception.BadRequestException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.github.appointmentsio.api.domain.session.service.SessionService.authorizedOrThrow;
import static com.github.appointmentsio.api.utils.Constraints.MESSAGES.DATES_INTERVAL_CANNOT_LONGER_THAN_YEARS;
import static com.github.appointmentsio.api.utils.Constraints.MESSAGES.SEARCH_DATE_INTERVAL_INVALID;
import static com.github.appointmentsio.api.utils.Messages.message;
import static com.github.appointmentsio.api.utils.Response.unauthorized;
import static java.time.temporal.ChronoUnit.YEARS;

@Service
public class findSummaryService {

    public findSummaryService(TimeEntryRepository repository) {
        this.repository = repository;
    }

    private final TimeEntryRepository repository;

    public Summary findSummaryByUserId(LocalDateTime start, LocalDateTime end, Long userId) {

        var authorized = authorizedOrThrow();

        if (!authorized.canRead(userId)) {
            throw unauthorized("Not authorized to read summary for this user");
        }

        var errors = new ArrayList<Error>();

        if (start.isAfter(end) || end.isBefore(start)) {
            errors.add(new Error("start_date or end_date", message(SEARCH_DATE_INTERVAL_INVALID)));
        }

        if (YEARS.between(start, end) > 1) {
            errors.add(new Error("start_date or end_date", message(DATES_INTERVAL_CANNOT_LONGER_THAN_YEARS, 1)));
        }

        if (!errors.isEmpty()) {
            throw new BadRequestException(errors);
        }

        var entries = repository
            .findTimeEntriesByUserIdAndBetweenStartAndEndDate(start, end, userId);

        return new Summary(entries);
    }
}

package com.github.appointmentsio.api.domain.timeentry.service;

import com.github.appointmentsio.api.domain.timeentry.model.Summary;
import com.github.appointmentsio.api.domain.timeentry.repository.TimeEntryRepository;
import com.github.appointmentsio.api.errors.model.FieldError;
import com.github.appointmentsio.api.errors.exception.BadRequestException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.github.appointmentsio.api.domain.session.service.SessionService.authorizedOrThrow;
import static com.github.appointmentsio.api.utils.Constraints.MESSAGES.*;
import static com.github.appointmentsio.api.utils.Messages.message;
import static com.github.appointmentsio.api.utils.Response.notFound;
import static com.github.appointmentsio.api.utils.Response.unauthorized;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.temporal.ChronoUnit.YEARS;

@Service
public class findSummaryService {

    public findSummaryService(
            TimeEntryRepository timeEntryRepository
    ) {
        this.timeEntryRepository = timeEntryRepository;
    }

    private final TimeEntryRepository timeEntryRepository;

    public Summary findSummaryByUserId(LocalDateTime start, LocalDateTime end, String userNanoid) {

        var authorized = authorizedOrThrow();

        if (!authorized.canRead(userNanoid)) {
            throw unauthorized(message(NOT_AUTHORIZED_TO_READ, "'summaries'"));
        }

        var errors = new ArrayList<FieldError>();

        if (start.isAfter(end) || end.isBefore(start)) {
            errors.add(new FieldError("start_date or end_date", message(SEARCH_DATE_INTERVAL_INVALID)));
        }

        if (YEARS.between(start, end) > 1) {
            errors.add(new FieldError("start_date or end_date", message(DATES_INTERVAL_CANNOT_LONGER_THAN_YEARS, 1)));
        }

        if (!errors.isEmpty()) {
            throw new BadRequestException(errors);
        }

        var entries = timeEntryRepository
                .findTimeEntriesByUserIdAndBetweenStartAndEndDate(start, end, userNanoid.getBytes(UTF_8));

        return new Summary(entries);
    }
}

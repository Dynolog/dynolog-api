package com.github.appointmentsio.api.domain.timeentry.service;

import com.github.appointmentsio.api.domain.timeentry.model.Summary;
import com.github.appointmentsio.api.domain.timeentry.repository.TimeEntryRepository;
import com.github.appointmentsio.api.domain.user.repository.UserRepository;
import com.github.appointmentsio.api.errors.Error;
import com.github.appointmentsio.api.errors.exception.BadRequestException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
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
            TimeEntryRepository timeEntryRepository,
            UserRepository userRepository
    ) {
        this.timeEntryRepository = timeEntryRepository;
        this.userRepository = userRepository;
    }

    private final TimeEntryRepository timeEntryRepository;
    private final UserRepository userRepository;

    public Summary findSummaryByUserId(LocalDateTime start, LocalDateTime end, String userNanoid) {
        var optional = userRepository.findOptionalIdByNanoid(userNanoid.getBytes(UTF_8));
        return optional.map(id -> findSummaryByUserId(start, end, id))
                .orElseThrow(() -> notFound("user not found"));
    }

    public Summary findSummaryByUserId(LocalDateTime start, LocalDateTime end, Long userId) {

        var authorized = authorizedOrThrow();

        if (!authorized.canRead(userId)) {
            throw unauthorized(message(NOT_AUTHORIZED_TO_READ, "'summaries'"));
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

        var entries = timeEntryRepository
                .findTimeEntriesByUserIdAndBetweenStartAndEndDate(start, end, userId);

        return new Summary(entries);
    }
}

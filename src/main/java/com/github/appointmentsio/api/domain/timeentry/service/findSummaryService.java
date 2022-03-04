package com.github.appointmentsio.api.domain.timeentry.service;

import static com.github.appointmentsio.api.utils.Constants.MESSAGES.DATES_INTERVAL_CANNOT_LONGER_THAN_YEARS;
import static com.github.appointmentsio.api.utils.Constants.MESSAGES.SEARCH_DATE_INTERVAL_INVALID;
import static com.github.appointmentsio.api.utils.Messages.message;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.temporal.ChronoUnit.YEARS;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.github.appointmentsio.api.domain.timeentry.model.Summary;
import com.github.appointmentsio.api.domain.timeentry.repository.TimeEntryRepository;
import com.github.appointmentsio.api.domain.timeentry.repository.springdata.TimeEntrySpringDataRepository;
import com.github.appointmentsio.api.errors.exception.BadRequestException;
import com.github.appointmentsio.api.errors.model.FieldError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class findSummaryService {

    @Autowired
    public findSummaryService(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    private final TimeEntryRepository timeEntryRepository;

    public Summary findSummaryByUserId(LocalDateTime start, LocalDateTime end, String userNanoid) {
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
                .findAll(start, end, userNanoid);

        return new Summary(entries);
    }
}

package com.github.throyer.appointments.domain.timeentry.service;

import com.github.throyer.appointments.domain.timeentry.model.Summary;
import com.github.throyer.appointments.domain.timeentry.repository.TimeEntryRepository;
import com.github.throyer.appointments.errors.Error;
import com.github.throyer.appointments.errors.exception.BadRequestException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static java.time.temporal.ChronoUnit.YEARS;

@Service
public class findSummaryService {

    public findSummaryService(TimeEntryRepository repository) {
        this.repository = repository;
    }

    private final TimeEntryRepository repository;

    public Summary findSummaryByUserId(LocalDateTime start, LocalDateTime end, Long userId) {

        var errors = new ArrayList<Error>();

        if (start.isAfter(end) || end.isBefore(start)) {
            errors.add(new Error("start_date or end_date", "start_date or end_date interval invalid"));
        }

        if (YEARS.between(start, end) > 1) {
            errors.add(new Error("interval", "The interval cannot be longer than 1 yar"));
        }

        if (!errors.isEmpty()) {
            throw new BadRequestException(errors);
        }

        var entries = repository
            .findTimeEntriesByUserIdAndBetweenStartAndEndDate(start, end, userId);

        return new Summary(entries);
    }
}

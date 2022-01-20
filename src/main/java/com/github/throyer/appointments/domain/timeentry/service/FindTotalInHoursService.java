package com.github.throyer.appointments.domain.timeentry.service;

import com.github.throyer.appointments.domain.timeentry.model.Summary;
import com.github.throyer.appointments.domain.timeentry.model.TimeEntryDetails;
import com.github.throyer.appointments.domain.timeentry.repository.TimeEntryRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class FindTotalInHoursService {

    public FindTotalInHoursService(TimeEntryRepository repository) {
        this.repository = repository;
    }

    private final TimeEntryRepository repository;

    public Summary findTotal(LocalDateTime start, LocalDateTime end, Long userId) {
        var entries = repository.findTimeEntriesByUserIdAndBetweenStartAndEndDate(start, end, userId);
        return new Summary(entries);
    }
}

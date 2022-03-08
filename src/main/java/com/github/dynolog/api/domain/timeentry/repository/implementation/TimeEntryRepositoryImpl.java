package com.github.dynolog.api.domain.timeentry.repository.implementation;

import com.github.dynolog.api.domain.pagination.Page;
import com.github.dynolog.api.domain.timeentry.entity.TimeEntry;
import com.github.dynolog.api.domain.timeentry.repository.TimeEntryRepository;
import com.github.dynolog.api.domain.timeentry.repository.querydsl.TimeEntryQueryDSLRepository;
import com.github.dynolog.api.domain.timeentry.repository.springdata.TimeEntrySpringDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;

@Repository
public class TimeEntryRepositoryImpl implements TimeEntryRepository {
    private final TimeEntryQueryDSLRepository dsl;
    private final TimeEntrySpringDataRepository spring;

    @Autowired
    public TimeEntryRepositoryImpl(
            TimeEntryQueryDSLRepository dsl,
            TimeEntrySpringDataRepository spring
    ) {
        this.dsl = dsl;
        this.spring = spring;
    }

    public List<TimeEntry> findAll(
            LocalDateTime start,
            LocalDateTime end,
            String userNanoId
    ) {
        return dsl.findAll(start, end, userNanoId);
    }

    public Page<TimeEntry> findAll(
            Pageable pageable,
            Optional<LocalDateTime> optionalStart,
            Optional<LocalDateTime> optionalEnd,
            String userNanoId
    ) {
        return dsl.findAll(pageable, optionalStart, optionalEnd, userNanoId);
    }

    public Optional<TimeEntry> findByNanoId(String nanoId) {
        return spring.findOptionalByNanoIdFetchUserAndProject(nanoId.getBytes(UTF_8));
    }

    public Optional<TimeEntry> findRunningByUserNanoId(String userNanoId) {
        return dsl.findRunningByUserNanoId(userNanoId);
    }

    public TimeEntry save(TimeEntry timeEntry) {
        return spring.save(timeEntry);
    }
}

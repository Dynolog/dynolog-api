package com.github.appointmentsio.api.domain.timeentry.repository;

import com.github.appointmentsio.api.domain.pagination.Page;
import com.github.appointmentsio.api.domain.timeentry.entity.TimeEntry;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeEntryRepository {
    List<TimeEntry> findAll(
            LocalDateTime start,
            LocalDateTime end,
            String userNanoId
    );

    Page<TimeEntry> findAll(
            Pageable pageable,
            Optional<LocalDateTime> optionalStart,
            Optional<LocalDateTime> optionalEnd,
            String userNanoId
    );

    Optional<TimeEntry> findByNanoId(String nanoId);

    Optional<TimeEntry> findRunningByUserNanoId(String userNanoId);

    TimeEntry save(TimeEntry timeEntry);
}

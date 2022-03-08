package com.github.dynolog.api.domain.timeentry.repository.springdata;

import com.github.dynolog.api.domain.shared.repository.NonSequentialIdRepository;
import com.github.dynolog.api.domain.timeentry.entity.TimeEntry;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TimeEntrySpringDataRepository extends NonSequentialIdRepository<TimeEntry> {
    @Query(Queries.FIND_BY_NANO_ID)
    Optional<TimeEntry> findOptionalByNanoIdFetchUserAndProject(
        @Param("time_entry_nano_id") byte[] timeEntryNanoId
    );
}

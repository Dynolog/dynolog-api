package com.github.appointmentsio.api.domain.timeentry.repository.springdata;

import com.github.appointmentsio.api.domain.shared.repository.NonSequentialIdRepository;
import com.github.appointmentsio.api.domain.timeentry.entity.TimeEntry;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.github.appointmentsio.api.domain.timeentry.repository.springdata.Queries.FIND_BY_NANO_ID;

@Repository
public interface TimeEntrySpringDataRepository extends NonSequentialIdRepository<TimeEntry> {
    @Query(FIND_BY_NANO_ID)
    Optional<TimeEntry> findOptionalByNanoIdFetchUserAndProject(
        @Param("time_entry_nano_id") byte[] timeEntryNanoId
    );
}

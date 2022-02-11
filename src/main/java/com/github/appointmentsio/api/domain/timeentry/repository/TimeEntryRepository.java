package com.github.appointmentsio.api.domain.timeentry.repository;

import com.github.appointmentsio.api.domain.shared.repository.NonSequentialIdRepository;
import com.github.appointmentsio.api.domain.timeentry.entity.TimeEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.github.appointmentsio.api.domain.timeentry.repository.Queries.*;

@Repository
public interface TimeEntryRepository extends NonSequentialIdRepository<TimeEntry> {

    @Query(FIND_TIME_ENTRIES_BY_USER_ID_FETCH_USER_AND_PROJECT)
    Page<TimeEntry> findAllByUserIdFetchUserAndProject(
            Pageable pageable,
            @Param("start_date") LocalDateTime start,
            @Param("end_date") LocalDateTime end,
            @Param("user_id") Long userId
    );

    @Query(FIND_TIME_ENTRY_BY_ID)
    TimeEntry findByIdFetchUserAndProject(@Param("id") Long id);

    @Query(FIND_BY_NANOID)
    Optional<TimeEntry> findOptionalByNanoidFetchUserAndProject(@Param("nanoid") byte[] nanoid);

    @Query(FIND_TIME_ENTRIES_BY_USER_ID_BETWEEN_START_AND_END_DATE)
    List<TimeEntry> findTimeEntriesByUserIdAndBetweenStartAndEndDate(
            @Param("start_date") LocalDateTime start,
            @Param("end_date") LocalDateTime end,
            @Param("user_id") Long userId
    );
}

package com.github.throyer.appointments.domain.timeentry.repository;

import com.github.throyer.appointments.domain.timeentry.model.TimeEntryDetails;
import com.github.throyer.appointments.domain.timeentry.entity.TimeEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.github.throyer.appointments.domain.timeentry.repository.Queries.*;

@Repository
public interface TimeEntryRepository extends JpaRepository<TimeEntry, Long> {

    @Query(FIND_TIME_ENTRIES_BY_USER_ID_FETCH_USER_AND_PROJECT)
    Page<TimeEntryDetails> findAllByUserIdFetchUserAndProject(
        Pageable pageable,
        @Param("start_date") LocalDateTime start,
        @Param("end_date") LocalDateTime end,
        @Param("user_id") Long userId
    );

    @Query(FIND_ALL_TIME_ENTRIES_FETCH_USER_AND_PROJECT)
    Page<TimeEntryDetails> findAllFetchUserAndProject(
            Pageable pageable,
            @Param("start_date") LocalDateTime start,
            @Param("end_date") LocalDateTime end
    );

    @Query(FIND_TIME_ENTRY_BY_ID)
    TimeEntryDetails findByIdFetchUserAndProject(@Param("id") Long id);

    @Query(FIND_TIME_ENTRIES_BY_USER_ID_BETWEEN_START_AND_END_DATE)
    List<TimeEntryDetails> findTimeEntriesByUserIdAndBetweenStartAndEndDate(
        @Param("start_date") LocalDateTime start,
        @Param("end_date") LocalDateTime end,
        @Param("user_id") Long userId
    );
}

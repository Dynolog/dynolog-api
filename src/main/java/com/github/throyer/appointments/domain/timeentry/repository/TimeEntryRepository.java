package com.github.throyer.appointments.domain.timeentry.repository;

import com.github.throyer.appointments.domain.timeentry.model.TimeEntryDetails;
import com.github.throyer.appointments.domain.timeentry.entity.TimeEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import static com.github.throyer.appointments.domain.timeentry.repository.Queries.FIND_TIME_ENTRIES_BY_USER_ID_FETCH_USER_AND_PROJECT;

@Repository
public interface TimeEntryRepository extends JpaRepository<TimeEntry, Long> {
    
    @Query(FIND_TIME_ENTRIES_BY_USER_ID_FETCH_USER_AND_PROJECT)
    public Page<TimeEntryDetails> findByUserIdFetchUserAndProject(Pageable pageable, @Param("id")Long userId);

    @Query(Queries.FIND_TIME_ENTRIES_ALL_USER_ID_FETCH_USER_AND_PROJECT)
    public Page<TimeEntryDetails> findAllFetchUserAndProject(Pageable pageable);
}

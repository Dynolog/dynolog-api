package com.github.throyer.appointments.domain.timeentry.repository;

import com.github.throyer.appointments.domain.timeentry.dto.TimeEntryDetails;
import com.github.throyer.appointments.domain.timeentry.entity.TimeEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeEntryRepository extends JpaRepository<TimeEntry, Long> {
    
    @Query("""
        SELECT
            new com.github.throyer.apontamentos.domain.timeentry.dto.TimeEntryDetails(
              time_entry.id,
              time_entry.description,
              time_entry.start,
              time_entry.stop
            )
        FROM TimeEntry AS time_entry
        WHERE time_entry.user.id = :id
    """)
    public Page<TimeEntryDetails> findByUserId(Pageable pageable, @Param("id")Long userId);
}

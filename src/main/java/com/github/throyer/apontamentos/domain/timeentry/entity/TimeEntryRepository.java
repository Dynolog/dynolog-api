package com.github.throyer.apontamentos.domain.timeentry.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeEntryRepository extends JpaRepository<TimeEntry, Long> { }

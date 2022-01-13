package com.github.throyer.appointments.domain.project.repository;

import com.github.throyer.appointments.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> { }

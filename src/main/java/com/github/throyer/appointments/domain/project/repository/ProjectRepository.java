package com.github.throyer.appointments.domain.project.repository;

import com.github.throyer.appointments.domain.project.entity.Project;
import com.github.throyer.appointments.domain.project.model.SimplifiedProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.github.throyer.appointments.domain.project.repository.Queries.FIND_ALL_FETCH_USER;
import static com.github.throyer.appointments.domain.project.repository.Queries.FIND_PROJECT_BY_ID_FETCH_USER;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query(FIND_PROJECT_BY_ID_FETCH_USER)
    Optional<Project> findOptionalByIdFetchUser(@Param("id") Long id);

    @Query(FIND_PROJECT_BY_ID_FETCH_USER)
    Project findByIdFetchUser(@Param("id") Long id);

    @Query(FIND_ALL_FETCH_USER)
    List<Project> findAllFetchUser(@Param("user_id") Long userId);
}

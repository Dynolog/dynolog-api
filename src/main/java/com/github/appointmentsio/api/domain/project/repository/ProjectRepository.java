package com.github.appointmentsio.api.domain.project.repository;

import com.github.appointmentsio.api.domain.project.entity.Project;
import com.github.appointmentsio.api.domain.shared.repository.NonSequentialIdRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.github.appointmentsio.api.domain.project.repository.Queries.*;

@Repository
public interface ProjectRepository extends NonSequentialIdRepository<Project> {
    @Query(FIND_PROJECT_BY_NANO_ID_FETCH_USER)
    Optional<Project> findOptionalByNanoIdFetchUser(@Param("project_nano_id") byte[] projectNanoId);

    @Query(FIND_ALL_BY_USER_NANO_ID_FETCH_USER)
    List<Project> findAllByUserNanoIdFetchUser(@Param("user_nano_id") byte[] userNanoId);
}

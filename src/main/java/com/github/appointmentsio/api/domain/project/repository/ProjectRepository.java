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

    @Query(FIND_PROJECT_BY_ID_FETCH_USER)
    Optional<Project> findOptionalByIdFetchUser(@Param("id") Long id);

    @Query(FIND_PROJECT_BY_NANOID_FETCH_USER)
    Optional<Project> findOptionalByIdFetchUser(@Param("nanoid") byte[] nanoid);

    @Query(FIND_PROJECT_BY_ID_FETCH_USER)
    Project findByIdFetchUser(@Param("id") Long id);

    @Query(FIND_ALL_FETCH_USER)
    List<Project> findAllFetchUser(@Param("nanoid") byte[] nanoid);
}

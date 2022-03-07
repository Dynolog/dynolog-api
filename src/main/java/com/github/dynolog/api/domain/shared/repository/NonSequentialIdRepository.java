package com.github.dynolog.api.domain.shared.repository;

import com.github.dynolog.api.domain.shared.model.NonSequentialId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

import static com.github.dynolog.api.domain.shared.repository.Queries.FIND_ID_BY_NANO_ID;

@NoRepositoryBean
public interface NonSequentialIdRepository<T extends NonSequentialId> extends JpaRepository<T, Long> {
    @Query(FIND_ID_BY_NANO_ID)
    Optional<Long> findOptionalIdByNanoid(byte[] nanoId);
}

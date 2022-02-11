package com.github.appointmentsio.api.domain.shared.repository;

import com.github.appointmentsio.api.domain.shared.model.NonSequentialId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

import static com.github.appointmentsio.api.domain.shared.repository.Queries.FIND_ID_BY_NANOID;

@NoRepositoryBean
public interface NonSequentialIdRepository<T extends NonSequentialId> extends JpaRepository<T, Long> {
    @Query(FIND_ID_BY_NANOID)
    Optional<Long> findOptionalIdByNanoid(byte[] nanoid);
}

package com.github.appointmentsio.api.domain.user.repository;

import com.github.appointmentsio.api.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import static com.github.appointmentsio.api.domain.shared.repository.Queries.FIND_ID_BY_NANOID;
import static com.github.appointmentsio.api.domain.user.repository.Queries.FIND_ALL_USERS_FETCH_ROLES;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public Boolean existsByEmail(String email);

    public Optional<User> findOptionalByEmail(String email);

    @Query(FIND_ID_BY_NANOID)
    Optional<Long> findOptionalIdByNanoid(byte[] nanoid);

    @Query(FIND_ALL_USERS_FETCH_ROLES)
    public Optional<User> findOptionalByEmailFetchRoles(String email);
}

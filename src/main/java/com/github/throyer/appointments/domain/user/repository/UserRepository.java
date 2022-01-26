package com.github.throyer.appointments.domain.user.repository;

import com.github.throyer.appointments.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import static com.github.throyer.appointments.domain.user.repository.Queries.FIND_ALL_USERS_FETCH_ROLES;
import static com.github.throyer.appointments.domain.user.repository.Queries.FIND_USER_DETAILS_BY_ID_FETCH_ROLES;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public Boolean existsByEmail(String email);

    public Optional<User> findOptionalByEmail(String email);

    @Query(FIND_ALL_USERS_FETCH_ROLES)
    public Optional<User> findOptionalByEmailFetchRoles(String email);
}

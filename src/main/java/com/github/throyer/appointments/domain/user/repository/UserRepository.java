package com.github.throyer.appointments.domain.user.repository;

import com.github.throyer.appointments.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public Boolean existsByEmail(String email);

    public Optional<User> findOptionalByEmail(String email);

    @Query("""
        SELECT user FROM User user
        LEFT JOIN FETCH user.roles
        WHERE user.email = ?1
    """)
    public Optional<User> findOptionalByEmailFetchRoles(String email);
}

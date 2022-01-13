package com.github.throyer.appointments.domain.session.repository;

import com.github.throyer.appointments.domain.session.entity.RefreshToken;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    @Transactional
    @Modifying
    @Query("""
        UPDATE
            RefreshToken
        SET
            available = false
        WHERE
            user_id = ?1 AND available = true
    """)
    public void disableOldRefreshTokens(Long id);

    @Query("""
        SELECT refresh FROM RefreshToken refresh
        JOIN FETCH refresh.user user
        JOIN FETCH user.roles
        WHERE refresh.code = ?1 AND refresh.available = true
    """)
    public Optional<RefreshToken> findOptionalByCodeAndAvailableIsTrue(String code);
}

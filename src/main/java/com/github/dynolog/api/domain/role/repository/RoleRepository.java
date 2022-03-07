package com.github.dynolog.api.domain.role.repository;

import com.github.dynolog.api.domain.role.entity.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    public Optional<Role> findOptionalByInitials(String initials);
}

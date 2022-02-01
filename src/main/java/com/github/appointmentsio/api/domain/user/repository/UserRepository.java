package com.github.appointmentsio.api.domain.user.repository;

import com.github.appointmentsio.api.domain.shared.repository.NonSequentialIdRepository;
import com.github.appointmentsio.api.domain.user.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends NonSequentialIdRepository<User> {
    Boolean existsByEmail(String email);
}

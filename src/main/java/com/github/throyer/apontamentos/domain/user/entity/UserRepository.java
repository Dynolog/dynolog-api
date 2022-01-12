package com.github.throyer.apontamentos.domain.user.entity;

import com.github.throyer.apontamentos.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> { }

package com.project.healthcomplex.security.repository;

import com.project.healthcomplex.security.model.Security;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecurityRepository extends JpaRepository<Security, Long> {
    Optional<Security> findByLogin(String login);
}
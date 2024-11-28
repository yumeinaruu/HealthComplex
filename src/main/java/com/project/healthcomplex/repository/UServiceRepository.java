package com.project.healthcomplex.repository;

import com.project.healthcomplex.model.UService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UServiceRepository extends JpaRepository<UService, Long> {
}

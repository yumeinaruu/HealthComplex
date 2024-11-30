package com.project.healthcomplex.repository;

import com.project.healthcomplex.model.DateTimeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface DateTimeRepository extends JpaRepository<DateTimeModel, Long> {
    Optional<DateTimeModel> findByStart(Timestamp start);
}

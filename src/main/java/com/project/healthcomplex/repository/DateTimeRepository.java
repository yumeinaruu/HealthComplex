package com.project.healthcomplex.repository;

import com.project.healthcomplex.model.DateTimeModel;
import com.project.healthcomplex.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DateTimeRepository extends JpaRepository<DateTimeModel, Long> {
}

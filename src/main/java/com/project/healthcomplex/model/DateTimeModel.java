package com.project.healthcomplex.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Entity(name = "date_time_model")
@Component
@Data
public class DateTimeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp start;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "u_service_id", nullable = false)
    private UService uService;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private Users user;
}

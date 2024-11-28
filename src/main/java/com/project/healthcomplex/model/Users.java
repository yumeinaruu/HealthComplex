package com.project.healthcomplex.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Collection;

@Entity(name = "users")
@Component
@Data
public class Users {
    @Id
    @SequenceGenerator(name = "usersIdSeqGen", sequenceName = "users_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "usersIdSeqGen")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "created", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp created;

    @Column(name = "changed")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp changed;
}
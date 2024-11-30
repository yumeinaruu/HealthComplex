package com.project.healthcomplex.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "created", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp created;

    @Column(name = "changed")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp changed;

    @ManyToMany
    private Collection<UService> uServices;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<DateTimeModel> assignedTimes;

    public void addUService(UService uService) {
        this.uServices.add(uService);
        uService.getUsers().add(this);
    }

    public void removeUService(UService uService) {
        this.uServices.remove(uService);
        uService.getUsers().remove(this);
    }
}
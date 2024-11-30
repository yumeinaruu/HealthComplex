package com.project.healthcomplex.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;

@Entity(name = "uservice")
@Component
@Data
public class UService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "duration", nullable = false)
    private String duration;

    @Column(name = "url")
    private String url;

    @Column(name = "created", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp created;

    @Column(name = "changed")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp changed;

    @ManyToMany
    @JsonIgnore
    private Collection<Users> users;

    @OneToMany(mappedBy = "uService", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<DateTimeModel> dateTimes;

    public void addUser(Users user) {
        this.users.add(user);
        user.getUServices().add(this);
    }

    public void removeUser(Users user) {
        this.users.remove(user);
        user.getUServices().remove(this);
    }
}

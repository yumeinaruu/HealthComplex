package com.project.healthcomplex.security.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;
import org.springframework.stereotype.Component;

@Entity(name = "security")
@Data
@Component
public class Security {
    @Id
    @SequenceGenerator(name = "securitySeqGen", sequenceName = "security_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "securitySeqGen")
    private Long id;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Roles role;

    @Column(name = "user_id", nullable = false)
    private Long userId;
}

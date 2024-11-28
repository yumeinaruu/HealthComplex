package com.project.healthcomplex.security.service;

import com.project.healthcomplex.security.model.Security;
import com.project.healthcomplex.security.repository.SecurityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class CustomUserDetailsService {
    private final SecurityRepository securityRepository;

    @Autowired
    public CustomUserDetailsService(SecurityRepository securityRepository) {
        this.securityRepository = securityRepository;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Security> securityInfoOptional = securityRepository.findByLogin(username);
        if (securityInfoOptional.isEmpty()) {
            throw new UsernameNotFoundException("Username not found: " + username);
        }
        Security securityInfo = securityInfoOptional.get();
        return User.builder()
                .username(securityInfo.getLogin())
                .password(securityInfo.getPassword())
                .roles(securityInfo.getRole().toString())
                .build();
    }
}

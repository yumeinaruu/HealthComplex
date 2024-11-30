package com.project.healthcomplex.security.service;


import com.project.healthcomplex.exception.custom.SameUserInDatabase;
import com.project.healthcomplex.model.Users;
import com.project.healthcomplex.repository.UserRepository;
import com.project.healthcomplex.security.model.Roles;
import com.project.healthcomplex.security.model.Security;
import com.project.healthcomplex.security.model.dto.AuthRequestDto;
import com.project.healthcomplex.security.model.dto.RegistrationDto;
import com.project.healthcomplex.security.repository.SecurityRepository;
import com.project.healthcomplex.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SecurityService {
    private final SecurityRepository securityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    EmailService emailService;

    @Autowired
    public SecurityService(SecurityRepository securityRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils,
                           EmailService emailService) {
        this.securityRepository = securityRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.emailService = emailService;
    }

    @Transactional(rollbackFor = Exception.class)
    public void registration(RegistrationDto registrationDto) {
        Optional<Security> security = securityRepository.findByLogin(registrationDto.getLogin());
        if (security.isPresent()) {
            throw new SameUserInDatabase(registrationDto.getLogin());
        }
        Users user = new Users();
        user.setName(registrationDto.getName());
        user.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        user.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        Users savedUser = userRepository.save(user);

        Security userSecurity = new Security();
        userSecurity.setLogin(registrationDto.getLogin());
        userSecurity.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        userSecurity.setRole(Roles.USER);
        userSecurity.setUserId(savedUser.getId());
        securityRepository.save(userSecurity);
        emailService.sendEmailNoAttachment(userSecurity.getLogin(), emailService.getCc(),
                "Регистрация в оздоровительном комплексе", emailService.getRegistrationBody()
                        + "\n Благодарим за участие в нашем проекте!");
    }

    @Transactional(rollbackFor = Exception.class)
    public void registrationForAdmin(RegistrationDto registrationDto) {
        Optional<Security> security = securityRepository.findByLogin(registrationDto.getLogin());
        if (security.isPresent()) {
            throw new SameUserInDatabase(registrationDto.getLogin());
        }
        Users user = new Users();
        user.setName(registrationDto.getName());
        user.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        user.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        Users savedUser = userRepository.save(user);

        Security userSecurity = new Security();
        userSecurity.setLogin(registrationDto.getLogin());
        userSecurity.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        userSecurity.setRole(Roles.ADMIN);
        userSecurity.setUserId(savedUser.getId());
        securityRepository.save(userSecurity);
        emailService.sendEmailNoAttachment(userSecurity.getLogin(), emailService.getCc(),
                "Регистрация администратора в оздоровительном комплексе", emailService.getRegistrationBody()
                        + "\n Ваш логин: " + registrationDto.getLogin() + "\n Ваш пароль: " + registrationDto.getPassword() +
                        "\n Не делитесь ни с кем этой информацией");
    }

    @Transactional(rollbackFor = Exception.class)
    public void registrationForCoach(RegistrationDto registrationDto) {
        Optional<Security> security = securityRepository.findByLogin(registrationDto.getLogin());
        if (security.isPresent()) {
            throw new SameUserInDatabase(registrationDto.getLogin());
        }
        Users user = new Users();
        user.setName(registrationDto.getName());
        user.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        user.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        Users savedUser = userRepository.save(user);

        Security userSecurity = new Security();
        userSecurity.setLogin(registrationDto.getLogin());
        userSecurity.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        userSecurity.setRole(Roles.COACH);
        userSecurity.setUserId(savedUser.getId());
        securityRepository.save(userSecurity);
        emailService.sendEmailNoAttachment(userSecurity.getLogin(), emailService.getCc(),
                "Регистрация тренера в оздоровительном комплексе", emailService.getRegistrationBody()
                        + "\n Ваш логин: " + registrationDto.getLogin() + "\n Ваш пароль: " + registrationDto.getPassword() +
                        "\n Не делитесь ни с кем этой информацией");
    }

    @Transactional(rollbackFor = Exception.class)
    public void registrationForCashier(RegistrationDto registrationDto) {
        Optional<Security> security = securityRepository.findByLogin(registrationDto.getLogin());
        if (security.isPresent()) {
            throw new SameUserInDatabase(registrationDto.getLogin());
        }
        Users user = new Users();
        user.setName(registrationDto.getName());
        user.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        user.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        Users savedUser = userRepository.save(user);

        Security userSecurity = new Security();
        userSecurity.setLogin(registrationDto.getLogin());
        userSecurity.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        userSecurity.setRole(Roles.CASHIER);
        userSecurity.setUserId(savedUser.getId());
        securityRepository.save(userSecurity);
        emailService.sendEmailNoAttachment(userSecurity.getLogin(), emailService.getCc(),
                "Регистрация кассира в оздоровительном комплексе", emailService.getRegistrationBody()
                        + "\n Ваш логин: " + registrationDto.getLogin() + "\n Ваш пароль: " + registrationDto.getPassword() +
                        "\n Не делитесь ни с кем этой информацией");
    }

    public Optional<String> generateToken(AuthRequestDto authRequestDto) {
        Optional<Security> security = securityRepository.findByLogin(authRequestDto.getLogin());
        if (security.isPresent()
                && passwordEncoder.matches(authRequestDto.getPassword(), security.get().getPassword())) {
            return Optional.of(jwtUtils.generateToken(authRequestDto.getLogin()));
        }
        return Optional.empty();
    }

    public Boolean giveAdmin(Long id) {
        Optional<Security> securityOptional = securityRepository.findById(id);
        if (securityOptional.isPresent()) {
            Security security = securityOptional.get();
            security.setRole(Roles.ADMIN);
            Security savedSecurity = securityRepository.saveAndFlush(security);
            return savedSecurity.equals(security);
        }
        return false;
    }

    public Boolean downgradeAdmin(Long id) {
        Optional<Security> securityOptional = securityRepository.findById(id);
        if (securityOptional.isPresent()) {
            Security security = securityOptional.get();
            security.setRole(Roles.USER);
            Security savedSecurity = securityRepository.saveAndFlush(security);
            return savedSecurity.equals(security);
        }
        return false;
    }
}


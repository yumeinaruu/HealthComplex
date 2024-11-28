package com.project.healthcomplex.service;

import com.project.healthcomplex.model.Users;
import com.project.healthcomplex.model.dto.users.UserCreateDto;
import com.project.healthcomplex.model.dto.users.UserUpdateDto;
import com.project.healthcomplex.repository.UserRepository;
import com.project.healthcomplex.security.model.Security;
import com.project.healthcomplex.security.repository.SecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final SecurityRepository securityRepository;

    @Autowired
    public UserService(UserRepository userRepository, SecurityRepository securityRepository) {
        this.userRepository = userRepository;
        this.securityRepository = securityRepository;
    }

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<Users> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<Users> getUserByName(String name) {
        return userRepository.findByName(name);
    }

    public Optional<Users> getInfoAboutCurrentUser(String username) {
        Optional<Security> security = securityRepository.findByLogin(username);
        if (security.isEmpty()) {
            return Optional.empty();
        }
        return userRepository.findById(security.get().getUserId());
    }

    public List<Users> getUsersSortedByName() {
        return userRepository.findAll(Sort.by("name"));
    }

    public Boolean createUser(UserCreateDto userCreateDto) {
        Users user = new Users();
        user.setName(userCreateDto.getName());
        user.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        user.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        Users savedUser = userRepository.save(user);
        return getUserById(savedUser.getId()).isPresent();
    }

    public Boolean updateUser(UserUpdateDto userUpdateDto) {
        Optional<Users> userOptional = userRepository.findById(userUpdateDto.getId());
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            user.setName(userUpdateDto.getName());
            user.setChanged(Timestamp.valueOf(LocalDateTime.now()));
            Users savedUser = userRepository.saveAndFlush(user);
            return savedUser.equals(user);
        }
        return false;
    }

    public Boolean deleteUserById(Long id) {
        Optional<Users> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return false;
        }
        userRepository.delete(userOptional.get());
        return true;
    }
}

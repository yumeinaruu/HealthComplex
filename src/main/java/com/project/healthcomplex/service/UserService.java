package com.project.healthcomplex.service;

import com.project.healthcomplex.model.UService;
import com.project.healthcomplex.model.Users;
import com.project.healthcomplex.model.dto.AssignDto;
import com.project.healthcomplex.model.dto.users.UserCreateDto;
import com.project.healthcomplex.model.dto.users.UserUpdateDto;
import com.project.healthcomplex.repository.UServiceRepository;
import com.project.healthcomplex.repository.UserRepository;
import com.project.healthcomplex.security.model.Security;
import com.project.healthcomplex.security.repository.SecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final SecurityRepository securityRepository;
    private final UServiceRepository uServiceRepository;

    @Autowired
    public UserService(UserRepository userRepository, SecurityRepository securityRepository, UServiceRepository uServiceRepository) {
        this.userRepository = userRepository;
        this.securityRepository = securityRepository;
        this.uServiceRepository = uServiceRepository;
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

    public Boolean assignUService(AssignDto assignDto) {
        Optional<UService> uServiceOptional = uServiceRepository.findById(assignDto.getService_id());
        Optional<Users> usersOptional = userRepository.findById(assignDto.getUser_id());

        if (usersOptional.isPresent() && uServiceOptional.isPresent()) {
            Users user = usersOptional.get();
            UService uService = uServiceOptional.get();

            user.addUService(uService);

            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    public Boolean unAssignUService(AssignDto assignDto) {
        Optional<UService> uServiceOptional = uServiceRepository.findById(assignDto.getService_id());
        Optional<Users> usersOptional = userRepository.findById(assignDto.getUser_id());

        if (usersOptional.isPresent() && uServiceOptional.isPresent()) {
            Users user = usersOptional.get();
            UService uService = uServiceOptional.get();

            user.removeUService(uService);

            userRepository.save(user);

            return true;
        } else {
            return false;
        }
    }


    public Boolean assignServiceToCurrentUser(Long serviceId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        Optional<Users> userOptional = userRepository.findByName(username);
        if (userOptional.isEmpty()) {
            return false;
        }

        Optional<UService> uServiceOptional = uServiceRepository.findById(serviceId);
        if (uServiceOptional.isEmpty()) {
            return false;
        }

        Users user = userOptional.get();
        UService uService = uServiceOptional.get();

        user.addUService(uService);

        userRepository.save(user);
        return true;
    }

    public Collection<UService> getServicesByUserId(Long userId) {
        Optional<Users> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            return userOptional.get().getUServices();
        }
        return Collections.emptyList();
    }

    public Collection<UService> getServicesOfCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Users> userOptional = getInfoAboutCurrentUser(username);
        if (userOptional.isPresent()) {
            return userOptional.get().getUServices();
        }
        return Collections.emptyList();
    }

    public Boolean deleteUserById(Long id) {
        Optional<Users> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return false;
        }
        Users user = userOptional.get();

        user.getUServices().forEach(service -> service.getUsers().remove(user));
        user.getUServices().clear();
        userRepository.delete(user);
        return true;
    }
}

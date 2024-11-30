package com.project.healthcomplex.service;

import com.project.healthcomplex.model.DateTimeModel;
import com.project.healthcomplex.model.UService;
import com.project.healthcomplex.model.Users;
import com.project.healthcomplex.model.dto.AssignDto;
import com.project.healthcomplex.model.dto.users.UserCreateDto;
import com.project.healthcomplex.model.dto.users.UserUpdateDto;
import com.project.healthcomplex.repository.DateTimeRepository;
import com.project.healthcomplex.repository.UServiceRepository;
import com.project.healthcomplex.repository.UserRepository;
import com.project.healthcomplex.security.model.Security;
import com.project.healthcomplex.security.repository.SecurityRepository;
import jakarta.transaction.Transactional;
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
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final SecurityRepository securityRepository;
    private final UServiceRepository uServiceRepository;
    private final DateTimeRepository dateTimeRepository;

    @Autowired
    public UserService(UserRepository userRepository, SecurityRepository securityRepository, UServiceRepository uServiceRepository,
                       DateTimeRepository dateTimeRepository) {
        this.userRepository = userRepository;
        this.securityRepository = securityRepository;
        this.uServiceRepository = uServiceRepository;
        this.dateTimeRepository = dateTimeRepository;
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

    public Boolean assignServiceToCurrentUser(String username, Long serviceId, Long timeId) {

        Optional<Security> security = securityRepository.findByLogin(username);
        if (security.isEmpty()) {
            return false;
        }
        Optional<Users> userOptional = userRepository.findById(security.get().getUserId());
        Optional<DateTimeModel> dateTimeOptional = dateTimeRepository.findById(timeId);

        if (userOptional.isPresent() && dateTimeOptional.isPresent()) {
            DateTimeModel dateTime = dateTimeOptional.get();

            if (!dateTime.getUService().getId().equals(serviceId)) {
                return false;
            }

            Users user = userOptional.get();
            dateTime.setUser(user);
            user.getAssignedTimes().add(dateTime);
            user.getUServices().add(dateTime.getUService());

            userRepository.save(user);
            return true;
        }
        return false;
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


    public Boolean assignServiceWithTimeToUser(Long userId, Long serviceId, Long timeId) {
        Optional<Users> userOptional = userRepository.findById(userId);
        Optional<DateTimeModel> dateTimeOptional = dateTimeRepository.findById(timeId);

        if (userOptional.isPresent() && dateTimeOptional.isPresent()) {
            DateTimeModel dateTime = dateTimeOptional.get();

            if (!dateTime.getUService().getId().equals(serviceId)) {
                return false;
            }

            Users user = userOptional.get();
            dateTime.setUser(user);
            user.getAssignedTimes().add(dateTime);
            user.getUServices().add(dateTime.getUService());

            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Transactional
    public Boolean unAssignServiceFromUser(Long userId, Long timeId) {
        Optional<Users> userOptional = userRepository.findById(userId);
        Optional<DateTimeModel> dateTimeOptional = dateTimeRepository.findById(timeId);

        if (userOptional.isPresent() && dateTimeOptional.isPresent()) {
            DateTimeModel dateTime = dateTimeOptional.get();

            if (!dateTime.getUser().getId().equals(userId)) {
                return false;
            }

            Users user = userOptional.get();
            user.getAssignedTimes().remove(dateTime);
            user.getUServices().remove(dateTime.getUService());

            dateTime.setUser(null);
            dateTimeRepository.save(dateTime);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public Boolean deleteUserById(Long id) {
        Optional<Users> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return false;
        }
        Users user = userOptional.get();

        user.getUServices().forEach(service -> service.getUsers().remove(user));
        user.getUServices().clear();
        user.getAssignedTimes().forEach(time -> time.setUser(null));
        user.getAssignedTimes().clear();
        userRepository.delete(user);
        return true;
    }
}

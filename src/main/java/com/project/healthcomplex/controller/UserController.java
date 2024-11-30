package com.project.healthcomplex.controller;

import com.project.healthcomplex.exception.custom.CustomValidationException;
import com.project.healthcomplex.model.DateTimeModel;
import com.project.healthcomplex.model.UService;
import com.project.healthcomplex.model.Users;
import com.project.healthcomplex.model.dto.AssignDto;
import com.project.healthcomplex.model.dto.FindByNameDto;
import com.project.healthcomplex.model.dto.users.UserCreateDto;
import com.project.healthcomplex.model.dto.users.UserUpdateDto;
import com.project.healthcomplex.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Пользователи")
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'COACH', 'CASHIER')")
    @Operation(summary = "Получить список всех пользователей")
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'COACH', 'CASHIER')")
    @Operation(summary = "Получить пользователя по id")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        Optional<Users> user = userService.getUserById(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{userId}/services")
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'COACH', 'CASHIER')")
    @Operation(summary = "Получить услуги пользователя по айди")
    public ResponseEntity<Collection<UService>> getUserServices(@PathVariable Long userId) {
        Collection<UService> services = userService.getServicesByUserId(userId);
        if (!services.isEmpty()) {
            return ResponseEntity.ok(services);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
    }

    @GetMapping("/services")
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'COACH', 'CASHIER')")
    @Operation(summary = "Получить услуги текущего пользователя")
    public ResponseEntity<Collection<UService>> getCurrentUserServices() {
        Collection<UService> services = userService.getServicesOfCurrentUser();
        if (!services.isEmpty()) {
            return ResponseEntity.ok(services);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
    }

    @PostMapping("/name")
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'COACH', 'CASHIER')")
    @Operation(summary = "Получить пользователя по имени")
    public ResponseEntity<Users> getUserByName(@RequestBody @Valid FindByNameDto findByNameDto,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        Optional<Users> user = userService.getUserByName(findByNameDto.getName());
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/name-sort")
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'COACH', 'CASHIER')")
    @Operation(summary = "Получить пользователей, отсортированных по имени")
    public ResponseEntity<List<Users>> getUsersSortedByName() {
        List<Users> users = userService.getUsersSortedByName();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/info")
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'COACH', 'CASHIER')")
    @Operation(summary = "Личный кабинет")
    public ResponseEntity<Users> getCurrentUser(Principal principal) {
        Optional<Users> result = userService.getInfoAboutCurrentUser(principal.getName());
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result.get(), HttpStatus.OK);
    }

    @PostMapping("/services/assign")
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'COACH', 'CASHIER')")
    @Operation(summary = "Подписаться на услугу")
    public ResponseEntity<String> assignServiceToCurrentUser(@RequestParam Long serviceId) {
        Boolean result = userService.assignServiceToCurrentUser(serviceId);
        if (result) {
            return ResponseEntity.ok("Услуга успешно назначена текущему пользователю.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка при назначении услуги.");
    }

    @PostMapping("/services/assign-to")
    @PreAuthorize("hasAnyRole('ADMIN', 'COACH', 'CASHIER')")
    @Operation(summary = "Подписать пользователя на услугу")
    public ResponseEntity<String> assignServiceToUser(@RequestBody AssignDto assignDto) {
        Boolean result = userService.assignUService(assignDto);
        if (result) {
            return ResponseEntity.ok("Услуга успешно назначена текущему пользователю.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка при назначении услуги.");
    }

    @PostMapping("/services/unassign")
    @PreAuthorize("hasAnyRole('ADMIN', 'COACH', 'CASHIER')")
    @Operation(summary = "Отписать пользователя от услуги")
    public ResponseEntity<String> unassignServiceFromUser(@RequestBody AssignDto assignDto) {
        Boolean result = userService.unAssignUService(assignDto);
        if (result) {
            return ResponseEntity.ok("Услуга успешно удалена у пользователя.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка при удалении услуги.");
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Обновление пользователя")
    public ResponseEntity<HttpStatus> updateUser(@RequestBody @Valid UserUpdateDto userUpdateDto,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        return new ResponseEntity<>(userService.updateUser(userUpdateDto) ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/{userId}/assign")
    public ResponseEntity<String> assignServiceWithTimeToUser(
            @PathVariable Long userId,
            @RequestParam Long serviceId,
            @RequestParam Long timeId
    ) {
        if (userService.assignServiceWithTimeToUser(userId, serviceId, timeId)) {
            return ResponseEntity.ok("Service and time assigned to user successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to assign service and time to user");
    }

    @DeleteMapping("/{userId}/unassign")
    public ResponseEntity<String> unAssignServiceFromUser(
            @PathVariable Long userId,
            @RequestParam Long timeId
    ) {
        if (userService.unAssignServiceFromUser(userId, timeId)) {
            return ResponseEntity.ok("Service and time unassigned from user successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to unassign service and time from user");
    }

//    @GetMapping("/{userId}/times")
//    public ResponseEntity<?> getUserAssignedTimes(@PathVariable Long userId) {
//        List<DateTimeModel> times = userService.getUserAssignedTimes(userId);
//        if (times.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No times assigned to user");
//        }
//        return ResponseEntity.ok(times);
//    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Удаление пользователя")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id) {
        return new ResponseEntity<>(userService.deleteUserById(id) ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST);
    }
}

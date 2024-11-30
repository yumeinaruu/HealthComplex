package com.project.healthcomplex.controller;

import com.project.healthcomplex.exception.custom.CustomValidationException;
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
@Tag(name = "Work with users")
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
    @Operation(summary = "Gives info about all users")
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'COACH', 'CASHIER')")
    @Operation(summary = "Gives info about user by id")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        Optional<Users> user = userService.getUserById(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{userId}/services")
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'COACH', 'CASHIER')")
    public ResponseEntity<Collection<UService>> getUserServices(@PathVariable Long userId) {
        Collection<UService> services = userService.getServicesByUserId(userId);
        if (!services.isEmpty()) {
            return ResponseEntity.ok(services);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
    }

    @GetMapping("/services")
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'COACH', 'CASHIER')")
    public ResponseEntity<Collection<UService>> getCurrentUserServices() {
        Collection<UService> services = userService.getServicesOfCurrentUser();
        if (!services.isEmpty()) {
            return ResponseEntity.ok(services);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
    }

    @PostMapping("/name")
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'COACH', 'CASHIER')")
    @Operation(summary = "Gives info about user by name")
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
    @Operation(summary = "Gives info about all users sorted by name")
    public ResponseEntity<List<Users>> getUsersSortedByName() {
        List<Users> users = userService.getUsersSortedByName();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/info")
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'COACH', 'CASHIER')")
    @Operation(summary = "Gives info about current logged user")
    public ResponseEntity<Users> getCurrentUser(Principal principal) {
        Optional<Users> result = userService.getInfoAboutCurrentUser(principal.getName());
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result.get(), HttpStatus.OK);
    }

    @PostMapping("/services/assign")
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'COACH', 'CASHIER')")
    public ResponseEntity<String> assignServiceToCurrentUser(@RequestParam Long serviceId) {
        Boolean result = userService.assignServiceToCurrentUser(serviceId);
        if (result) {
            return ResponseEntity.ok("Услуга успешно назначена текущему пользователю.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка при назначении услуги.");
    }

    @PostMapping("/services/assign-to")
    @PreAuthorize("hasAnyRole('ADMIN', 'COACH', 'CASHIER')")
    public ResponseEntity<String> assignServiceToUser(@RequestBody AssignDto assignDto) {
        Boolean result = userService.assignUService(assignDto);
        if (result) {
            return ResponseEntity.ok("Услуга успешно назначена текущему пользователю.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка при назначении услуги.");
    }

    @PostMapping("/services/unassign")
    @PreAuthorize("hasAnyRole('ADMIN', 'COACH', 'CASHIER')")
    public ResponseEntity<String> unassignServiceFromUser(@RequestBody AssignDto assignDto) {
        Boolean result = userService.unAssignUService(assignDto);
        if (result) {
            return ResponseEntity.ok("Услуга успешно удалена у пользователя.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка при удалении услуги.");
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Create user")
    public ResponseEntity<HttpStatus> createUser(@RequestBody @Valid UserCreateDto userCreateDto,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        return new ResponseEntity<>(userService.createUser(userCreateDto) ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Update user")
    public ResponseEntity<HttpStatus> updateUser(@RequestBody @Valid UserUpdateDto userUpdateDto,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        return new ResponseEntity<>(userService.updateUser(userUpdateDto) ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Delete user")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id) {
        return new ResponseEntity<>(userService.deleteUserById(id) ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST);
    }
}

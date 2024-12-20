package com.project.healthcomplex.security.controller;

import com.project.healthcomplex.exception.custom.CustomValidationException;
import com.project.healthcomplex.security.model.dto.AuthRequestDto;
import com.project.healthcomplex.security.model.dto.AuthResponseDto;
import com.project.healthcomplex.security.model.dto.RegistrationDto;
import com.project.healthcomplex.security.service.SecurityService;
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

import java.util.Optional;

@RestController
@Tag(name = "Безопасность")
@RequestMapping("/security")
@SecurityRequirement(name = "Bearer Authentication")
public class SecurityController {
    private final SecurityService securityService;

    @Autowired
    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @PostMapping("/registration")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Регистрация пользователя")
    public ResponseEntity<HttpStatus> registration(@RequestBody @Valid RegistrationDto registrationDto,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        securityService.registration(registrationDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/registration/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Регистрация админа")
    public ResponseEntity<HttpStatus> registrationForAdmin(@RequestBody @Valid RegistrationDto registrationDto,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        securityService.registrationForAdmin(registrationDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/registration/cashier")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Регистрация кассира")
    public ResponseEntity<HttpStatus> registrationForCashier(@RequestBody @Valid RegistrationDto registrationDto,
                                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        securityService.registrationForCashier(registrationDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/registration/coach")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Регистрация тренера")
    public ResponseEntity<HttpStatus> registrationForCoach(@RequestBody @Valid RegistrationDto registrationDto,
                                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        securityService.registrationForCoach(registrationDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/token")
    @Operation(summary = "Создание токена")
    public ResponseEntity<AuthResponseDto> generateToken(@RequestBody @Valid AuthRequestDto authRequest,
                                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        Optional<String> token = securityService.generateToken(authRequest);
        if (token.isPresent()) {
            return new ResponseEntity<>(new AuthResponseDto(token.get()), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/give-admin/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Повышение до админа")
    public ResponseEntity<HttpStatus> giveAdmin(@PathVariable Long id) {
        return new ResponseEntity<>(securityService.giveAdmin(id) ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/downgrade-admin/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Понижение до пользователя")
    public ResponseEntity<HttpStatus> downgradeAdmin(@PathVariable Long id) {
        return new ResponseEntity<>(securityService.downgradeAdmin(id) ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST);
    }
}

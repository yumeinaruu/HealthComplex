package com.project.healthcomplex.controller;

import com.project.healthcomplex.exception.custom.CustomValidationException;
import com.project.healthcomplex.model.UService;
import com.project.healthcomplex.model.dto.FindByNameDto;
import com.project.healthcomplex.model.dto.uservice.UServiceCreateDto;
import com.project.healthcomplex.model.dto.uservice.UServiceUpdateDto;
import com.project.healthcomplex.service.UServiceService;
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

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Work with service")
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/service")
public class UServiceController {
    private final UServiceService uServiceService;

    @Autowired
    public UServiceController(UServiceService uServiceService) {
        this.uServiceService = uServiceService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'COACH', 'CASHIER')")
    @Operation(summary = "Получить информацию о всех услугах")
    public ResponseEntity<List<UService>> getAllUServices() {
        List<UService> uServices = uServiceService.getAllUServices();
        if (uServices.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(uServices, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'COACH', 'CASHIER')")
    @Operation(summary = "Получить информацию об услуге по айди")
    public ResponseEntity<UService> getUServiceById(@PathVariable Long id) {
        Optional<UService> uService = uServiceService.getUServiceById(id);
        if (uService.isPresent()) {
            return new ResponseEntity<>(uService.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/name")
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'COACH', 'CASHIER')")
    @Operation(summary = "Получить услугу по имени")
    public ResponseEntity<UService> getUServiceByName(@RequestBody @Valid FindByNameDto findByNameDto,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        Optional<UService> uService = uServiceService.getUServiceByName(findByNameDto.getName());
        if (uService.isPresent()) {
            return new ResponseEntity<>(uService.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/name-sort")
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'COACH', 'CASHIER')")
    @Operation(summary = "Получить услуги, отсортированные по имени")
    public ResponseEntity<List<UService>> getUServicesSortedByName() {
        List<UService> uServices = uServiceService.getUServicesSortedByName();
        if (uServices.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(uServices, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'COACH')")
    @Operation(summary = "Создать услугу")
    public ResponseEntity<HttpStatus> createUService(@RequestBody @Valid UServiceCreateDto uServiceCreateDto,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        return new ResponseEntity<>(uServiceService.createUService(uServiceCreateDto) ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'COACH')")
    @Operation(summary = "Обновить услугу")
    public ResponseEntity<HttpStatus> updateUService(@RequestBody @Valid UServiceUpdateDto uServiceUpdateDto,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        return new ResponseEntity<>(uServiceService.updateUService(uServiceUpdateDto) ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/{serviceId}/times")
    public ResponseEntity<String> addTimeToService(
            @PathVariable Long serviceId,
            @RequestParam("startTime") Timestamp startTime
    ) {
        if (uServiceService.addTimeToService(serviceId, startTime)) {
            return ResponseEntity.ok("Time added to service successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COACH')")
    @Operation(summary = "Удалить услугу")
    public ResponseEntity<HttpStatus> deleteUService(@PathVariable Long id) {
        return new ResponseEntity<>(uServiceService.deleteUServiceById(id) ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST);
    }
}

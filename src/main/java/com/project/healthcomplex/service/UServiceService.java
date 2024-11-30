package com.project.healthcomplex.service;

import com.project.healthcomplex.model.UService;
import com.project.healthcomplex.model.dto.uservice.UServiceCreateDto;
import com.project.healthcomplex.model.dto.uservice.UServiceUpdateDto;
import com.project.healthcomplex.repository.UServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UServiceService {
    private final UServiceRepository uServiceRepository;

    @Autowired
    public UServiceService(UServiceRepository uServiceRepository) {
        this.uServiceRepository = uServiceRepository;
    }

    public List<UService> getAllUServices() {
        return uServiceRepository.findAll();
    }

    public Optional<UService> getUServiceById(Long id) {
        return uServiceRepository.findById(id);
    }

    public Optional<UService> getUServiceByName(String name) {
        return uServiceRepository.findByName(name);
    }

    public List<UService> getUServicesSortedByName() {
        return uServiceRepository.findAll(Sort.by("name"));
    }

    public Boolean createUService(UServiceCreateDto uServiceCreateDto) {
        UService uService = new UService();
        uService.setName(uServiceCreateDto.getName());
        uService.setDuration(uServiceCreateDto.getDuration());
        uService.setPrice(uServiceCreateDto.getPrice());
        uService.setUrl(uServiceCreateDto.getUrl());
        uService.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        uService.setChanged(Timestamp.valueOf(LocalDateTime.now()));

        UService savedUService = uServiceRepository.save(uService);
        return getUServiceById(savedUService.getId()).isPresent();
    }

    public Boolean updateUService(UServiceUpdateDto uServiceUpdateDto) {
        Optional<UService> uServiceOptional = uServiceRepository.findById(uServiceUpdateDto.getId());
        if (uServiceOptional.isPresent()) {
            UService uService = uServiceOptional.get();
            uService.setName(uServiceUpdateDto.getName());
            uService.setDuration(uServiceUpdateDto.getDuration());
            uService.setPrice(uServiceUpdateDto.getPrice());
            uService.setUrl(uServiceUpdateDto.getUrl());
            uService.setChanged(Timestamp.valueOf(LocalDateTime.now()));
            UService savedUService = uServiceRepository.saveAndFlush(uService);
            return savedUService.equals(uService);
        }
        return false;
    }

    public Boolean deleteUServiceById(Long id) {
        Optional<UService> uServiceOptional = uServiceRepository.findById(id);
        if (uServiceOptional.isEmpty()) {
            return false;
        }
        uServiceRepository.delete(uServiceOptional.get());
        return true;
    }
}

package com.project.healthcomplex.service;

import com.project.healthcomplex.model.UService;
import com.project.healthcomplex.model.Users;
import com.project.healthcomplex.repository.UServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}

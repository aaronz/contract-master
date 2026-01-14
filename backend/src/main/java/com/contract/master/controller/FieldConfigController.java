package com.contract.master.controller;

import com.contract.master.entity.FieldConfig;
import com.contract.master.repository.FieldConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/settings/fields")
public class FieldConfigController {

    @Autowired
    private FieldConfigRepository fieldConfigRepository;

    @GetMapping
    public List<FieldConfig> list() {
        return fieldConfigRepository.findAll();
    }

    @PostMapping
    public void save(@RequestBody List<FieldConfig> configs) {
        fieldConfigRepository.saveAll(configs);
    }
}

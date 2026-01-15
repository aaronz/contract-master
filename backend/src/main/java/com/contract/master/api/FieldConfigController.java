package com.contract.master.api;

import com.contract.master.domain.FieldConfig;
import com.contract.master.service.FieldConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/configs")
public class FieldConfigController {

    @Autowired
    private FieldConfigService fieldConfigService;

    @GetMapping
    public List<FieldConfig> getConfigs() {
        return fieldConfigService.getConfigs();
    }

    @PostMapping
    public void saveConfig(@RequestBody FieldConfig config) {
        fieldConfigService.saveConfig(config);
    }
}

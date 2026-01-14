package com.contract.master.controller;

import com.contract.master.entity.SearchTemplate;
import com.contract.master.repository.SearchTemplateRepository;
import com.contract.master.security.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/settings/search-templates")
public class SearchTemplateController {

    @Autowired
    private SearchTemplateRepository repository;

    @GetMapping
    public List<SearchTemplate> list() {
        return repository.findByTenantIdAndUserId(TenantContext.getCurrentTenant(), "admin");
    }

    @PostMapping
    public SearchTemplate save(@RequestBody SearchTemplate template) {
        template.setUserId("admin");
        return repository.save(template);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}

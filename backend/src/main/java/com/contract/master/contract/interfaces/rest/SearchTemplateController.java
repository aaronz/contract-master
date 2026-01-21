package com.contract.master.contract.interfaces.rest;

import com.contract.master.api.GlobalExceptionHandler;
import com.contract.master.contract.domain.model.SearchTemplate;
import com.contract.master.contract.domain.repository.SearchTemplateRepository;
import com.contract.master.security.TenantContext;
import com.contract.master.shared.domain.model.TenantId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/settings/search-templates")
public class SearchTemplateController {

    @Autowired
    private SearchTemplateRepository repository;

    @GetMapping
    public GlobalExceptionHandler.ApiResponse<List<SearchTemplate>> list() {
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, repository.findByUserId("admin"));
    }

    @PostMapping
    public GlobalExceptionHandler.ApiResponse<SearchTemplate> save(@RequestBody SearchTemplate template) {
        template.setUserId("admin");
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, repository.save(template));
    }

    @DeleteMapping("/{id}")
    public GlobalExceptionHandler.ApiResponse<Void> delete(@PathVariable Long id) {
        repository.deleteById(id);
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, null);
    }
}

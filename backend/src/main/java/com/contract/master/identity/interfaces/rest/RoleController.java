package com.contract.master.identity.interfaces.rest;

import com.contract.master.api.GlobalExceptionHandler;
import com.contract.master.identity.domain.model.Role;
import com.contract.master.identity.domain.repository.RoleRepository;
import com.contract.master.security.TenantContext;
import com.contract.master.shared.domain.model.TenantId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping
    public GlobalExceptionHandler.ApiResponse<List<Role>> list() {
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, roleRepository.findByTenantId(TenantId.of(TenantContext.getCurrentTenant())));
    }

    @PostMapping
    public GlobalExceptionHandler.ApiResponse<Role> create(@RequestBody Role role) {
        if (role.getRoleId() == null) {
            role.setRoleId(UUID.randomUUID().toString());
        }
        role.setTenantId(TenantId.of(TenantContext.getCurrentTenant()));
        role.setStatus(1);
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, roleRepository.save(role));
    }

    @DeleteMapping("/{id}")
    public GlobalExceptionHandler.ApiResponse<Void> delete(@PathVariable Long id) {
        roleRepository.deleteById(id);
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, null);
    }
}

package com.contract.master.identity.interfaces.rest;

import com.contract.master.api.GlobalExceptionHandler;
import com.contract.master.identity.application.UserService;
import com.contract.master.identity.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public GlobalExceptionHandler.ApiResponse<List<User>> list() {
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, userService.getAllUsers());
    }

    @GetMapping("/{id}/roles")
    public GlobalExceptionHandler.ApiResponse<List<String>> getUserRoles(@PathVariable String id) {
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, userService.getUserRoleIds(id));
    }

    @PostMapping("/{id}/roles")
    public GlobalExceptionHandler.ApiResponse<Void> assignRoles(@PathVariable String id, @RequestBody List<String> roleIds) {
        userService.assignRoles(id, roleIds);
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, null);
    }
}

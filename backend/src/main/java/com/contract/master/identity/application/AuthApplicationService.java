package com.contract.master.identity.application;

import com.contract.master.identity.dto.AuthResponse;
import com.contract.master.identity.dto.LoginRequest;
import com.contract.master.security.JwtTokenProvider;
import com.contract.master.security.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.stream.Collectors;
import java.util.List;

@Service
public class AuthApplicationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private com.contract.master.identity.domain.repository.UserRoleRelRepository userRoleRelRepository;

    public AuthResponse authenticate(LoginRequest request) {
        if (request.getTenantId() != null) {
            TenantContext.setCurrentTenant(request.getTenantId());
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            
            List<String> roles = userRoleRelRepository.findByUserIdAndTenantId(
                request.getUsername(), 
                com.contract.master.shared.domain.model.TenantId.of(request.getTenantId())
            ).stream().map(com.contract.master.identity.domain.model.UserRoleRel::getRoleId).collect(Collectors.toList());
            
            if ("admin".equals(request.getUsername()) && roles.isEmpty()) {
                roles.add("admin");
            }

            String token = jwtTokenProvider.generateToken(authentication);
            return new AuthResponse(token);
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        } finally {
            TenantContext.clear();
        }
    }
}

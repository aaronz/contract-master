package com.contract.master.service;

import com.contract.master.dto.AuthResponse;
import com.contract.master.dto.LoginRequest;
import com.contract.master.security.JwtTokenProvider;
import com.contract.master.security.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public AuthResponse authenticate(LoginRequest request) {
        if (request.getTenantId() != null) {
            TenantContext.setCurrentTenant(request.getTenantId());
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            String token = jwtTokenProvider.generateToken(authentication);
            return AuthResponse.builder().token(token).build();
        } finally {
            TenantContext.clear();
        }
    }
}

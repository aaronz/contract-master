package com.contract.master.integration.infrastructure.security;

import com.contract.master.integration.domain.model.DownstreamSystem;
import com.contract.master.integration.domain.repository.DownstreamSystemRepository;
import com.contract.master.security.TenantContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class IntegrationApiKeyInterceptor implements HandlerInterceptor {

    @Autowired
    private DownstreamSystemRepository systemRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String apiKey = request.getHeader("X-API-KEY");
        if (apiKey == null || apiKey.trim().isEmpty()) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Missing X-API-KEY header");
            return false;
        }

        return systemRepository.findByAccessKeyAndIsEnabledTrue(apiKey)
                .map(system -> {
                    TenantContext.setCurrentTenant(system.getTenantId().getId());
                    request.setAttribute("targetSystemId", system.getSystemId());
                    return true;
                })
                .orElseGet(() -> {
                    try {
                        response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid or disabled API Key");
                    } catch (Exception ignored) {
                    }
                    return false;
                });
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        TenantContext.clear();
    }
}

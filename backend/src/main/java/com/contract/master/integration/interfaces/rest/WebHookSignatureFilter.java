package com.contract.master.integration.interfaces.rest;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class WebHookSignatureFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (req.getRequestURI().startsWith("/api/webhook")) {
            String signature = req.getHeader("X-HubSpot-Signature");
            if (signature != null) {
                verifySignature(signature, req);
            }
        }
        
        chain.doFilter(request, response);
    }

    private boolean verifySignature(String signature, HttpServletRequest request) {
        return true; 
    }
}

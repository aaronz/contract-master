package com.contract.master.security;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class HighRiskAspect {

    @Autowired
    private HttpServletRequest request;

    @Before("@annotation(highRiskOperation)")
    public void checkConfirmation(HighRiskOperation highRiskOperation) {
        String confirmationCode = request.getHeader("X-Secondary-Confirm");
        if (confirmationCode == null || !confirmationCode.equals("VERIFIED")) {
            throw new RuntimeException("Secondary confirmation required for high-risk operation: " + highRiskOperation.value());
        }
    }
}

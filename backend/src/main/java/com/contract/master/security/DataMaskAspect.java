package com.contract.master.security;

import com.contract.master.utils.MaskUtils;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Collection;

@Aspect
@Component
public class DataMaskAspect {

    @AfterReturning(pointcut = "execution(* com.contract.master.api..*(..))", returning = "result")
    public void maskResult(Object result) {
        if (result == null) return;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"));

        if (isAdmin) return;
        
        if (result instanceof Collection<?>) {
            for (Object obj : (Collection<?>) result) {
                applyMask(obj);
            }
        } else {
            applyMask(result);
        }
    }

    private void applyMask(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(DataMask.class)) {
                DataMask mask = field.getAnnotation(DataMask.class);
                field.setAccessible(true);
                try {
                    Object val = field.get(obj);
                    if (val instanceof String) {
                        field.set(obj, MaskUtils.mask((String) val, mask.type()));
                    }
                } catch (Exception e) {
                }
            }
        }
    }
}

package com.contract.master.config;

import com.contract.master.security.TenantContext;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.RecordInterceptor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class KafkaTenantInterceptor implements RecordInterceptor<String, String> {

    @Override
    public ConsumerRecord<String, String> intercept(ConsumerRecord<String, String> record, Consumer<String, String> consumer) {
        byte[] tenantIdHeader = record.headers().lastHeader("tenant-id") != null 
            ? record.headers().lastHeader("tenant-id").value() 
            : null;
            
        if (tenantIdHeader != null) {
            String tenantId = new String(tenantIdHeader, StandardCharsets.UTF_8);
            TenantContext.setCurrentTenant(tenantId);
        }
        return record;
    }

    @Override
    public void afterRecord(ConsumerRecord<String, String> record, Consumer<String, String> consumer) {
        TenantContext.clear();
    }
}

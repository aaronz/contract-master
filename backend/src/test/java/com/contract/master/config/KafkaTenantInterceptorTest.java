package com.contract.master.config;

import com.contract.master.security.TenantContext;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class KafkaTenantInterceptorTest {

    private final KafkaTenantInterceptor interceptor = new KafkaTenantInterceptor();

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void shouldRestoreTenantContextFromHeader() {
        // Given
        RecordHeaders headers = new RecordHeaders();
        headers.add(new RecordHeader("tenant-id", "tenant-kafka-test".getBytes(StandardCharsets.UTF_8)));
        ConsumerRecord<String, String> record = new ConsumerRecord<>("test-topic", 0, 0, "key", "value");
        record.headers().add(new RecordHeader("tenant-id", "tenant-kafka-test".getBytes(StandardCharsets.UTF_8)));

        // When
        interceptor.intercept(record, mock(org.apache.kafka.clients.consumer.Consumer.class));

        // Then
        assertThat(TenantContext.getCurrentTenant()).isEqualTo("tenant-kafka-test");
    }

    @Test
    void shouldClearContextIfHeaderMissing() {
        // Given
        TenantContext.setCurrentTenant("old-tenant");
        ConsumerRecord<String, String> record = new ConsumerRecord<>("test-topic", 0, 0, "key", "value");

        // When
        interceptor.intercept(record, mock(org.apache.kafka.clients.consumer.Consumer.class));

        // Then
        assertThat(TenantContext.getCurrentTenant()).isNull();
    }
}

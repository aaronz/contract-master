package com.contract.master.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationJobDTO {
    private String jobId;
    private String tenantId;
    private String status;
    private String triggerType; // Corresponds to EvaluationJob.TriggerType
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private String triggeredBy;
    private List<String> targetRules; // Deserialized from target_rules JSON string
    private List<String> targetContracts; // Deserialized from target_contracts JSON string
    private Map<String, Object> results; // Not directly from model, but useful for UI
}
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
    private String contractId;
    private List<String> ruleIds;
    private String status;
    private String triggeredBy;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private Map<String, Object> results;
    private String tenantId;
}
package com.contract.master.evaluation.api;

import com.contract.master.evaluation.model.EvaluationJob;
import com.contract.master.evaluation.service.EvaluationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import com.contract.master.security.TenantContext;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/evaluations")
public class EvaluationController {

    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    // Request DTO for triggering evaluation
    public static class TriggerEvaluationRequest {
        private List<String> ruleIds;
        private List<String> contractIds;

        public List<String> getRuleIds() {
            return ruleIds;
        }

        public void setRuleIds(List<String> ruleIds) {
            this.ruleIds = ruleIds;
        }

        public List<String> getContractIds() {
            return contractIds;
        }

        public void setContractIds(List<String> contractIds) {
            this.contractIds = contractIds;
        }
    }

    @PostMapping
    public ResponseEntity<EvaluationJob> triggerEvaluation(@RequestBody TriggerEvaluationRequest request) {
        String tenantId = TenantContext.getCurrentTenant();
        String triggeredBy = SecurityContextHolder.getContext().getAuthentication().getName();

        if (request.getContractIds() == null || request.getContractIds().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        EvaluationJob job = evaluationService.createAndPublishEvaluationJob(
            request.getRuleIds() != null ? request.getRuleIds() : Collections.emptyList(),
            request.getContractIds(),
            tenantId,
            triggeredBy
        );

        return new ResponseEntity<>(job, HttpStatus.ACCEPTED);
    }
}

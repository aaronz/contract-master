package com.contractmaster.evaluation.api;

import com.contractmaster.evaluation.model.EvaluationJob;
import com.contractmaster.evaluation.service.EvaluationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID; // Assuming we generate a UUID for the job for now.

@RestController
@RequestMapping("/api/evaluations")
public class EvaluationController {

    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) { // Constructor updated
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
        // Placeholder for tenantId and triggeredBy, to be fetched from security context
        String tenantId = "default-tenant"; // Replace with actual tenant ID from security context
        String triggeredBy = "user-test"; // Replace with actual user ID from security context

        // For now, let's assume we're processing a single rule, as per T012 goal.
        // Batch processing will be handled in T022
        if (request.getRuleIds() == null || request.getRuleIds().isEmpty() || request.getContractIds() == null || request.getContractIds().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Use the EvaluationService to create and publish the job
        EvaluationJob job = evaluationService.createAndPublishEvaluationJob(
            request.getRuleIds(),
            request.getContractIds(),
            tenantId,
            triggeredBy
        );

        return new ResponseEntity<>(job, HttpStatus.ACCEPTED);
    }
}

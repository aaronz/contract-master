package com.contract.master.problemcenter.interfaces.rest;

import com.contract.master.api.GlobalExceptionHandler;
import com.contract.master.problemcenter.application.EvaluationService;
import com.contract.master.security.TenantContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/problem-center/evaluations")
public class ProblemEvaluationController {

    private final EvaluationService evaluationService;

    public ProblemEvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @PostMapping
    public GlobalExceptionHandler.ApiResponse<Long> triggerEvaluation(@RequestBody EvaluationTriggerRequest request) {
        Long jobId = evaluationService.startEvaluation(request.getContractId());
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.ACCEPTED, jobId);
    }

    public static class EvaluationTriggerRequest {
        private UUID contractId;

        public UUID getContractId() { return contractId; }
        public void setContractId(UUID contractId) { this.contractId = contractId; }
    }
}

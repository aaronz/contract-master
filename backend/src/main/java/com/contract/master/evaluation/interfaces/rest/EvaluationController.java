package com.contract.master.evaluation.interfaces.rest;

import com.contract.master.evaluation.application.EvaluationApplicationService;
import com.contract.master.evaluation.dto.EvaluationTriggerRequest;
import com.contract.master.api.GlobalExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/evaluations")
public class EvaluationController {

    @Autowired
    private EvaluationApplicationService evaluationService;

    @PostMapping
    public ResponseEntity<GlobalExceptionHandler.ApiResponse<?>> triggerEvaluation(
            @RequestBody EvaluationTriggerRequest request,
            Authentication authentication) {
        try {
            String triggeredBy = "anonymous";
            if (authentication != null && authentication.isAuthenticated()) {
                triggeredBy = authentication.getName();
            }
            
            if (request.getContractIds() == null || request.getContractIds().size() != 1) {
                return new ResponseEntity<>(GlobalExceptionHandler.ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "Exactly one contract ID is required for re-evaluation."), HttpStatus.BAD_REQUEST);
            }
            String contractId = request.getContractIds().get(0);

            String jobId = evaluationService.triggerReEvaluationForSingleContract(contractId, request.getRuleIds(), triggeredBy);
            return new ResponseEntity<>(GlobalExceptionHandler.ApiResponse.success(HttpStatus.ACCEPTED, Map.of("jobId", jobId)), HttpStatus.ACCEPTED);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return new ResponseEntity<>(GlobalExceptionHandler.ApiResponse.error(HttpStatus.CONFLICT.value(), e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(GlobalExceptionHandler.ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

package com.contract.master.evaluation.api;

import com.contract.master.evaluation.service.EvaluationService;
import com.contract.master.dto.EvaluationTriggerRequest;
import com.contract.master.api.GlobalExceptionHandler; // Assuming GlobalExceptionHandler is in api package
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // Assuming Spring Security for user details
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/evaluations")
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    @PostMapping
    public ResponseEntity<GlobalExceptionHandler.ApiResponse<Map<String, String>>> triggerEvaluation(
            @RequestBody EvaluationTriggerRequest request,
            Authentication authentication) { // Inject Authentication to get current user
        try {
            String triggeredBy = "anonymous"; // Default if authentication is not available
            if (authentication != null && authentication.isAuthenticated()) {
                triggeredBy = authentication.getName(); // Get username or user ID
            }
            String jobId = evaluationService.triggerEvaluation(request.getContractId(), request.getRuleIds(), triggeredBy);
            return new ResponseEntity<>(GlobalExceptionHandler.ApiResponse.success(Map.of("jobId", jobId)), HttpStatus.ACCEPTED);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return new ResponseEntity<>(GlobalExceptionHandler.ApiResponse.error(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(GlobalExceptionHandler.ApiResponse.error("Internal server error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
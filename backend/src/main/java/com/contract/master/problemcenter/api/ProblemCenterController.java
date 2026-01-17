package com.contract.master.problemcenter.api;

import com.contract.master.evaluation.domain.model.EvaluationJob;
import com.contract.master.evaluation.domain.model.EvaluationResult; // Import EvaluationResult
import com.contract.master.evaluation.domain.repository.EvaluationJobRepository;
import com.contract.master.evaluation.domain.repository.EvaluationResultRepository; // Import EvaluationResultRepository
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problem-center")
public class ProblemCenterController {

    private final EvaluationJobRepository evaluationJobRepository;
    private final EvaluationResultRepository evaluationResultRepository; // Inject EvaluationResultRepository

    public ProblemCenterController(EvaluationJobRepository evaluationJobRepository, EvaluationResultRepository evaluationResultRepository) {
        this.evaluationJobRepository = evaluationJobRepository;
        this.evaluationResultRepository = evaluationResultRepository;
    }

    @GetMapping("/evaluation-jobs")
    public ResponseEntity<List<EvaluationJob>> getEvaluationJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        if (size < 1) {
            throw new IllegalArgumentException("Page size must not be less than one");
        }

        // Placeholder for tenantId, to be fetched from security context
        String tenantId = "default-tenant"; // Replace with actual tenant ID from security context

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<EvaluationJob> evaluationJobs = evaluationJobRepository.findByTenantId(tenantId, pageable);

        return ResponseEntity.ok(evaluationJobs.getContent());
    }

    @GetMapping("/evaluation-jobs/{jobId}/results")
    public ResponseEntity<List<EvaluationResult>> getEvaluationResults(
            @PathVariable String jobId) {
        // Placeholder for tenantId, to be fetched from security context
        String tenantId = "default-tenant"; // Replace with actual tenant ID from security context

        List<EvaluationResult> results = evaluationResultRepository.findByJobIdAndTenantId(jobId, tenantId);
        return ResponseEntity.ok(results);
    }
}

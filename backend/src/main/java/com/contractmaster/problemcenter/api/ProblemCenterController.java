package com.contractmaster.problemcenter.api;

import com.contractmaster.evaluation.model.EvaluationJob;
import com.contractmaster.evaluation.model.EvaluationResult; // Import EvaluationResult
import com.contractmaster.evaluation.repository.EvaluationJobRepository;
import com.contractmaster.evaluation.repository.EvaluationResultRepository; // Import EvaluationResultRepository
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

        // Placeholder for tenantId, to be fetched from security context
        String tenantId = "default-tenant"; // Replace with actual tenant ID from security context

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<EvaluationJob> evaluationJobs = evaluationJobRepository.findByTenantIdOrderByCreatedAtDesc(tenantId, pageable);

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

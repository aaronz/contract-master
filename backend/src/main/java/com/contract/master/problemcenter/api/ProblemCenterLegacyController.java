package com.contract.master.problemcenter.api;

import com.contract.master.evaluation.domain.model.EvaluationJob;
import com.contract.master.evaluation.domain.model.EvaluationResult;
import com.contract.master.evaluation.domain.repository.EvaluationJobRepository;
import com.contract.master.evaluation.domain.repository.EvaluationResultRepository;
import com.contract.master.shared.domain.model.TenantId;
import com.contract.master.security.TenantContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problem-center-legacy")
public class ProblemCenterLegacyController {

    private final EvaluationJobRepository evaluationJobRepository;
    private final EvaluationResultRepository evaluationResultRepository;

    public ProblemCenterLegacyController(EvaluationJobRepository evaluationJobRepository, EvaluationResultRepository evaluationResultRepository) {
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

        String tenantIdStr = TenantContext.getCurrentTenant();
        if (tenantIdStr == null) {
            tenantIdStr = "default-tenant";
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<EvaluationJob> evaluationJobs = evaluationJobRepository.findByTenantId(tenantIdStr, pageable);



        return ResponseEntity.ok(evaluationJobs.getContent());
    }

    @GetMapping("/evaluation-jobs/{jobId}/results")
    public ResponseEntity<List<EvaluationResult>> getEvaluationResults(
            @PathVariable String jobId) {
        String tenantIdStr = TenantContext.getCurrentTenant();
        if (tenantIdStr == null) {
            tenantIdStr = "default-tenant";
        }

        List<EvaluationResult> results = evaluationResultRepository.findByJobIdAndTenantId(jobId, tenantIdStr);
        return ResponseEntity.ok(results);
    }
}

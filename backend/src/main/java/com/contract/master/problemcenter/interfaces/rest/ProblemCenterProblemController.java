package com.contract.master.problemcenter.interfaces.rest;

import com.contract.master.api.GlobalExceptionHandler;
import com.contract.master.problemcenter.application.ProblemService;
import com.contract.master.problemcenter.domain.model.Problem;
import com.contract.master.problemcenter.domain.model.ProblemStatus;
import com.contract.master.problemcenter.domain.repository.ProblemRepository;
import com.contract.master.shared.domain.model.TenantId;
import com.contract.master.security.TenantContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/problems")
public class ProblemCenterProblemController {

    private final ProblemRepository problemRepository;
    private final ProblemService problemService;

    public ProblemCenterProblemController(ProblemRepository problemRepository, ProblemService problemService) {
        this.problemRepository = problemRepository;
        this.problemService = problemService;
    }

    @GetMapping
    public GlobalExceptionHandler.ApiResponse<List<Problem>> list(
            @RequestParam(required = false) UUID contractId,
            @RequestParam(required = false) ProblemStatus status) {
        TenantId tenantId = TenantId.of(TenantContext.getCurrentTenant());
        List<Problem> problems;

        if (contractId != null && status != null) {
            problems = problemRepository.findByContractIdAndStatusAndTenantId(contractId, status, tenantId);
        } else if (contractId != null) {
            problems = problemRepository.findByContractIdAndTenantId(contractId, tenantId);
        } else {
            // For now, return all tenant problems - can be optimized with pagination later
            problems = problemRepository.findAll().stream()
                    .filter(p -> p.getTenantId().getId().equals(tenantId.getId()))
                    .toList();
        }

        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, problems);
    }

    @GetMapping("/{id}")
    public GlobalExceptionHandler.ApiResponse<Problem> get(@PathVariable Long id) {
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, problemRepository.findById(id).orElse(null));
    }

    @PutMapping("/{id}")
    public GlobalExceptionHandler.ApiResponse<Problem> update(@PathVariable Long id, @RequestBody ProblemUpdateRequest request) {
        Problem updated = problemService.updateStatus(id, request.getStatus(), request.getNotes());
        updated.setAssigneeId(request.getAssigneeId());
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, problemRepository.save(updated));
    }

    public static class ProblemUpdateRequest {
        private ProblemStatus status;
        private String assigneeId;
        private String notes;

        public ProblemStatus getStatus() { return status; }
        public void setStatus(ProblemStatus status) { this.status = status; }
        public String getAssigneeId() { return assigneeId; }
        public void setAssigneeId(String assigneeId) { this.assigneeId = assigneeId; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }
}

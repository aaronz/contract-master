package com.contract.master.problemcenter.application;

import com.contract.master.audit.application.AuditService;
import com.contract.master.problemcenter.domain.model.Problem;
import com.contract.master.problemcenter.domain.model.ProblemStatus;
import com.contract.master.problemcenter.domain.repository.ProblemRepository;
import com.contract.master.security.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final AuditService auditService;

    public ProblemService(ProblemRepository problemRepository, AuditService auditService) {
        this.problemRepository = problemRepository;
        this.auditService = auditService;
    }

    @Transactional
    public Problem updateStatus(Long id, ProblemStatus newStatus, String notes) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Problem not found: " + id));

        if (!isValidTransition(problem.getStatus(), newStatus)) {
            throw new IllegalArgumentException("Invalid status transition from " +
                    problem.getStatus() + " to " + newStatus);
        }

        ProblemStatus oldStatus = problem.getStatus();

        problem.setStatus(newStatus);
        if (notes != null && !notes.isEmpty()) {
            problem.setNotes(notes);
        }

        Problem saved = problemRepository.save(problem);

        auditService.logChange(
                problem.getContractId().toString(),
                "status",
                oldStatus.toString(),
                newStatus.toString(),
                AuditService.MANUAL,
                TenantContext.getCurrentTenant()
        );

        return saved;
    }

    private boolean isValidTransition(ProblemStatus from, ProblemStatus to) {
        switch (from) {
            case NEW:
                return to == ProblemStatus.ACKNOWLEDGED ||
                       to == ProblemStatus.IGNORED ||
                       to == ProblemStatus.RESOLVED;
            case ACKNOWLEDGED:
                return to == ProblemStatus.RESOLVED || to == ProblemStatus.IGNORED;
            case RESOLVED:
            case IGNORED:
                return false;
            default:
                return false;
        }
    }
}

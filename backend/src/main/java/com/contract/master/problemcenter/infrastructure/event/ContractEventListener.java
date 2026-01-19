package com.contract.master.problemcenter.infrastructure.event;

import com.contract.master.contract.domain.event.ContractSavedEvent;
import com.contract.master.problemcenter.application.EvaluationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ContractEventListener {

    private static final Logger log = LoggerFactory.getLogger(ContractEventListener.class);
    private final EvaluationService evaluationService;

    public ContractEventListener(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @Async
    @EventListener
    public void handleContractSaved(ContractSavedEvent event) {
        log.info("Received ContractSavedEvent for contract {}. Triggering evaluation.", event.getContractId());
        try {
            com.contract.master.security.TenantContext.setCurrentTenant(event.getTenantId());
            evaluationService.startEvaluation(event.getContractId(), null);
        } catch (Exception e) {
            log.error("Failed to trigger evaluation on contract saved event", e);
        } finally {
            com.contract.master.security.TenantContext.clear();
        }
    }
}

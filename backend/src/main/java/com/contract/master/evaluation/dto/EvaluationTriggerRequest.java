package com.contract.master.evaluation.dto;

import java.util.List;
import java.util.Objects;

public class EvaluationTriggerRequest {
    private List<String> contractIds;
    private List<String> ruleIds;

    public EvaluationTriggerRequest() {}

    public EvaluationTriggerRequest(List<String> contractIds, List<String> ruleIds) {
        this.contractIds = contractIds;
        this.ruleIds = ruleIds;
    }

    public List<String> getContractIds() {
        return contractIds;
    }

    public void setContractIds(List<String> contractIds) {
        this.contractIds = contractIds;
    }

    public List<String> getRuleIds() {
        return ruleIds;
    }

    public void setRuleIds(List<String> ruleIds) {
        this.ruleIds = ruleIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EvaluationTriggerRequest that = (EvaluationTriggerRequest) o;
        return Objects.equals(contractIds, that.contractIds) && Objects.equals(ruleIds, that.ruleIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contractIds, ruleIds);
    }
}

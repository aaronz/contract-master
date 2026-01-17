package com.contract.master.dto;

import lombok.Data;

import java.util.List;

@Data
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
}

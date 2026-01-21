package com.contract.master.problemcenter.application;

import com.contract.master.contract.domain.model.Contract;
import com.contract.master.problemcenter.domain.model.ProblemEvaluationJob;
import com.contract.master.problemcenter.domain.model.Problem;
import com.contract.master.problemcenter.domain.model.ProblemStatus;
import com.contract.master.rule.domain.model.Rule;
import org.springframework.stereotype.Component;

@Component
public class ProblemGenerator {

    public Problem generate(ProblemEvaluationJob job, Rule rule, Contract contract, String highlightedText, String locationJson) {
        Problem problem = new Problem();
        problem.setEvaluationJobId(job.getId());
        problem.setRuleId(rule.getId());
        problem.setContractId(contract.getContractId().value().toString());
        problem.setHighlightedText(highlightedText);
        problem.setLocationInContract(locationJson);
        problem.setGeneratedMessage("Violation of rule: " + rule.getName() + ". " + rule.getDescription());
        problem.setStatus(ProblemStatus.NEW);
        return problem;
    }
}

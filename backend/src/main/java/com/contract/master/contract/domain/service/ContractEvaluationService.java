package com.contract.master.contract.domain.service;

import com.contract.master.contract.domain.model.Contract;
import com.contract.master.rule.domain.model.Rule;
import java.util.List;

public interface ContractEvaluationService {
    void evaluate(Contract contract, List<Rule> rules);
}

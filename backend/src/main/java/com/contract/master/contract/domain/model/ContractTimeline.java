package com.contract.master.contract.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.time.LocalDate;

@Embeddable
@Getter
public class ContractTimeline {
    @Column(name = "sign_date")
    private LocalDate signDate;

    @Column(name = "effective_date")
    private LocalDate effectiveDate;

    @Column(name = "expire_date")
    private LocalDate expireDate;

    protected ContractTimeline() {}

    public ContractTimeline(LocalDate signDate, LocalDate effectiveDate, LocalDate expireDate) {
        this.signDate = signDate;
        this.effectiveDate = effectiveDate;
        this.expireDate = expireDate;
    }
}

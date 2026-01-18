package com.contract.master.contract.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class ContractTimeline {
    @Column(name = "sign_date")
    private LocalDate signDate;

    @Column(name = "effective_date")
    private LocalDate effectiveDate;

    @Column(name = "expire_date")
    private LocalDate expireDate;

    @Column(name = "performance_start_date")
    private LocalDate performanceStartDate;

    @Column(name = "performance_end_date")
    private LocalDate performanceEndDate;

    protected ContractTimeline() {}

    public ContractTimeline(LocalDate signDate, LocalDate effectiveDate, LocalDate expireDate, LocalDate performanceStartDate, LocalDate performanceEndDate) {
        this.signDate = signDate;
        this.effectiveDate = effectiveDate;
        this.expireDate = expireDate;
        this.performanceStartDate = performanceStartDate;
        this.performanceEndDate = performanceEndDate;
    }

    public LocalDate getSignDate() { return signDate; }
    public void setSignDate(LocalDate signDate) { this.signDate = signDate; }
    public LocalDate getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(LocalDate effectiveDate) { this.effectiveDate = effectiveDate; }
    public LocalDate getExpireDate() { return expireDate; }
    public void setExpireDate(LocalDate expireDate) { this.expireDate = expireDate; }
    public LocalDate getPerformanceStartDate() { return performanceStartDate; }
    public void setPerformanceStartDate(LocalDate performanceStartDate) { this.performanceStartDate = performanceStartDate; }
    public LocalDate getPerformanceEndDate() { return performanceEndDate; }
    public void setPerformanceEndDate(LocalDate performanceEndDate) { this.performanceEndDate = performanceEndDate; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContractTimeline that = (ContractTimeline) o;
        return Objects.equals(signDate, that.signDate) && Objects.equals(effectiveDate, that.effectiveDate) && Objects.equals(expireDate, that.expireDate) && Objects.equals(performanceStartDate, that.performanceStartDate) && Objects.equals(performanceEndDate, that.performanceEndDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(signDate, effectiveDate, expireDate, performanceStartDate, performanceEndDate);
    }
}

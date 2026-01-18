package com.contract.master.contract.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class ContractPayment {
    @Column(name = "payment_method", length = 32)
    private String method;

    @Column(name = "payment_term", columnDefinition = "TEXT")
    private String term;

    protected ContractPayment() {}

    public ContractPayment(String method, String term) {
        this.method = method;
        this.term = term;
    }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    public String getTerm() { return term; }
    public void setTerm(String term) { this.term = term; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContractPayment that = (ContractPayment) o;
        return Objects.equals(method, that.method) && Objects.equals(term, that.term);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, term);
    }
}

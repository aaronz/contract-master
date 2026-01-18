package com.contract.master.contract.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class ContractInvoice {
    @Column(name = "invoice_type", length = 32)
    private String type;

    @Column(name = "invoice_title", length = 128)
    private String title;

    @Column(name = "taxpayer_id", length = 64)
    private String taxpayerId;

    protected ContractInvoice() {}

    public ContractInvoice(String type, String title, String taxpayerId) {
        this.type = type;
        this.title = title;
        this.taxpayerId = taxpayerId;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getTaxpayerId() { return taxpayerId; }
    public void setTaxpayerId(String taxpayerId) { this.taxpayerId = taxpayerId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContractInvoice that = (ContractInvoice) o;
        return Objects.equals(type, that.type) && Objects.equals(title, that.title) && Objects.equals(taxpayerId, that.taxpayerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, title, taxpayerId);
    }
}

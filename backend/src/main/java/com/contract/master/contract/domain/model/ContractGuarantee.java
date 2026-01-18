package com.contract.master.contract.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class ContractGuarantee {
    @Column(name = "guarantee_flag")
    private Boolean flag;

    @Column(name = "guarantee_type", length = 32)
    private String type;

    @Column(name = "guarantor_info", columnDefinition = "TEXT")
    private String guarantorInfo;

    protected ContractGuarantee() {}

    public ContractGuarantee(Boolean flag, String type, String guarantorInfo) {
        this.flag = flag;
        this.type = type;
        this.guarantorInfo = guarantorInfo;
    }

    public Boolean getFlag() { return flag; }
    public void setFlag(Boolean flag) { this.flag = flag; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getGuarantorInfo() { return guarantorInfo; }
    public void setGuarantorInfo(String guarantorInfo) { this.guarantorInfo = guarantorInfo; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContractGuarantee that = (ContractGuarantee) o;
        return Objects.equals(flag, that.flag) && Objects.equals(type, that.type) && Objects.equals(guarantorInfo, that.guarantorInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flag, type, guarantorInfo);
    }
}

package com.contract.master.contract.domain.model;

import com.contract.master.shared.domain.base.ValueObject;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import java.util.Objects;

@Embeddable
public class ContractNo implements ValueObject {
    @Column(name = "contract_no", length = 64)
    private String value;

    protected ContractNo() {
        this.value = null;
    }

    public ContractNo(String value) {
        Objects.requireNonNull(value, "Contract number must not be null");
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException("Contract number must not be empty");
        }
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContractNo that = (ContractNo) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}

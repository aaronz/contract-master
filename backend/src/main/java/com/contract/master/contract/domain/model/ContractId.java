package com.contract.master.contract.domain.model;

import com.contract.master.shared.domain.base.ValueObject;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class ContractId implements ValueObject {
    @Column(name = "contract_id")
    private UUID value;

    protected ContractId() {
        this.value = null;
    }

    public ContractId(UUID value) {
        Objects.requireNonNull(value, "Contract ID value must not be null");
        this.value = value;
    }

    public static ContractId of(String id) {
        return new ContractId(UUID.fromString(id));
    }

    public static ContractId generate() {
        return new ContractId(UUID.randomUUID());
    }

    public UUID value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContractId that = (ContractId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value == null ? "" : value.toString();
    }
}

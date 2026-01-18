package com.contract.master.shared.domain.model;

import com.contract.master.shared.domain.base.ValueObject;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import java.util.Objects;

@Embeddable
public class TenantId implements ValueObject {
    @Column(name = "tenant_id", length = 64)
    private String id;

    protected TenantId() {
        this.id = null;
    }

    private TenantId(String id) {
        this.id = id;
    }

    public String getId() { return id; }
    public static TenantId of(String id) {
        return new TenantId(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TenantId tenantId = (TenantId) o;
        return Objects.equals(id, tenantId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id;
    }
}

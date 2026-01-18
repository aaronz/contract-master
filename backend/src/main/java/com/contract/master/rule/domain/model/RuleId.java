package com.contract.master.rule.domain.model;

import com.contract.master.shared.domain.base.ValueObject;
import java.util.Objects;
import java.util.UUID;

public record RuleId(UUID value) implements ValueObject {
    public RuleId {
        Objects.requireNonNull(value, "Rule ID value must not be null");
    }

    public static RuleId of(String id) {
        return new RuleId(UUID.fromString(id));
    }

    public static RuleId generate() {
        return new RuleId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

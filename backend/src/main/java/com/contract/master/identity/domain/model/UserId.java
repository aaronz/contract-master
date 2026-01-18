package com.contract.master.identity.domain.model;

import com.contract.master.shared.domain.base.ValueObject;
import java.util.Objects;
import java.util.UUID;

public record UserId(UUID value) implements ValueObject {
    public UserId {
        Objects.requireNonNull(value, "User ID value must not be null");
    }

    public static UserId of(String id) {
        return new UserId(UUID.fromString(id));
    }

    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

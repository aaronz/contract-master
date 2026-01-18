package com.contract.master.contract.domain.model;

import com.contract.master.shared.domain.base.ValueObject;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public record ExtendFieldData(
        UUID fieldId,
        String fieldCode,
        Object value,
        String fillType,
        String fillUser,
        LocalDateTime fillTime) implements ValueObject {
    public ExtendFieldData {
        Objects.requireNonNull(fieldId, "Field ID cannot be null");
        Objects.requireNonNull(fieldCode, "Field code cannot be null");
    }
}

package com.contract.master.shared.domain.model;

import com.contract.master.shared.domain.base.ValueObject;

import java.util.Objects;

public record PartyInfo(
        String partyId,
        String partyName,
        String contactPerson,
        String contactPhone,
        String address) implements ValueObject {

    public PartyInfo {
        Objects.requireNonNull(partyId, "Party ID cannot be null");
        Objects.requireNonNull(partyName, "Party name cannot be null");
        if (partyId.trim().isEmpty()) {
            throw new IllegalArgumentException("Party ID cannot be empty");
        }
        if (partyName.trim().isEmpty()) {
            throw new IllegalArgumentException("Party name cannot be empty");
        }
    }
}

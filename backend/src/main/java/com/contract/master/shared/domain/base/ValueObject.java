package com.contract.master.shared.domain.base;

// Marker interface for Value Objects.
// Value Objects measure, quantify, or describe a thing in the domain.
// They are immutable and are compared by their attributes, not identity.
public interface ValueObject {
    // Value Objects typically override equals() and hashCode() to compare by value.
    // They usually do not have an identity.
}

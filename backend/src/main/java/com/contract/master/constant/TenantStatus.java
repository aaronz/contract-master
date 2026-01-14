package com.contract.master.constant;

public enum TenantStatus {
    INACTIVE(0),
    ACTIVE(1),
    DISABLED(2);

    private final int value;

    TenantStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

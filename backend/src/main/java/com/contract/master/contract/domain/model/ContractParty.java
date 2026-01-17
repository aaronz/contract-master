package com.contract.master.contract.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class ContractParty {
    @Column(length = 64)
    private String id;

    @Column(length = 128)
    private String name;

    @Column(length = 64)
    private String contact;

    @Column(length = 32)
    private String phone;

    @Column(length = 512)
    private String address;

    protected ContractParty() {}

    public ContractParty(String id, String name, String contact, String phone, String address) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.phone = phone;
        this.address = address;
    }
}

package com.contract.master.contract.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class ContractParty {
    @Column(length = 64)
    private String partyIdentifier; // Renamed from 'id'

    @Column(length = 128)
    private String name;

    @Column(length = 64)
    private String contact;

    @Column(length = 32)
    private String phone;

    @Column(length = 512)
    private String address;

    protected ContractParty() {}

    public ContractParty(String partyIdentifier, String name, String contact, String phone, String address) {
        this.partyIdentifier = partyIdentifier;
        this.name = name;
        this.contact = contact;
        this.phone = phone;
        this.address = address;
    }

    public String getPartyIdentifier() { return partyIdentifier; }
    public void setPartyIdentifier(String partyIdentifier) { this.partyIdentifier = partyIdentifier; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContractParty that = (ContractParty) o;
        return Objects.equals(partyIdentifier, that.partyIdentifier) && Objects.equals(name, that.name) && Objects.equals(contact, that.contact) && Objects.equals(phone, that.phone) && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(partyIdentifier, name, contact, phone, address);
    }
}

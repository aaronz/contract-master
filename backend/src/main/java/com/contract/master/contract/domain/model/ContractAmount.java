package com.contract.master.contract.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class ContractAmount {
    @Column(name = "amount", precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(name = "tax_rate", precision = 5, scale = 2)
    private BigDecimal taxRate;

    @Column(name = "tax_amount", precision = 18, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "total_amount_with_tax", precision = 18, scale = 2)
    private BigDecimal totalAmountWithTax;

    @Column(name = "currency_type", length = 10)
    private String currencyType;

    protected ContractAmount() {}

    public ContractAmount(BigDecimal amount, BigDecimal taxRate, BigDecimal taxAmount, BigDecimal totalAmountWithTax, String currencyType) {
        this.amount = amount;
        this.taxRate = taxRate;
        this.taxAmount = taxAmount;
        this.totalAmountWithTax = totalAmountWithTax;
        this.currencyType = currencyType;
    }

    public static ContractAmount of(BigDecimal amount, String currency) {
        return new ContractAmount(amount, BigDecimal.ZERO, BigDecimal.ZERO, amount, currency);
    }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public BigDecimal getTaxRate() { return taxRate; }
    public void setTaxRate(BigDecimal taxRate) { this.taxRate = taxRate; }
    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }
    public BigDecimal getTotalAmountWithTax() { return totalAmountWithTax; }
    public void setTotalAmountWithTax(BigDecimal totalAmountWithTax) { this.totalAmountWithTax = totalAmountWithTax; }
    public String getCurrencyType() { return currencyType; }
    public void setCurrencyType(String currencyType) { this.currencyType = currencyType; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContractAmount that = (ContractAmount) o;
        return Objects.equals(amount, that.amount) && Objects.equals(taxRate, that.taxRate) && Objects.equals(taxAmount, that.taxAmount) && Objects.equals(totalAmountWithTax, that.totalAmountWithTax) && Objects.equals(currencyType, that.currencyType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, taxRate, taxAmount, totalAmountWithTax, currencyType);
    }
}

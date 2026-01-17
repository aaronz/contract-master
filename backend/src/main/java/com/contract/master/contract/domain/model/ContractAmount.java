package com.contract.master.contract.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
@Getter
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
}

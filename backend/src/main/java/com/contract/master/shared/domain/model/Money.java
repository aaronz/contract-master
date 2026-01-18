package com.contract.master.shared.domain.model;

import com.contract.master.shared.domain.base.ValueObject;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Money implements ValueObject {
    @Column(name = "amount", precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency_type", length = 10)
    private String currency;

    protected Money() {
        this.amount = null;
        this.currency = null;
    }

    public Money(BigDecimal amount, String currency) {
        Objects.requireNonNull(amount, "Amount cannot be null");
        Objects.requireNonNull(currency, "Currency cannot be null");
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if (currency.trim().isEmpty()) {
            throw new IllegalArgumentException("Currency cannot be empty");
        }
        this.amount = amount;
        this.currency = currency;
    }

    public static Money of(BigDecimal amount, String currency) {
        return new Money(amount, currency);
    }

    public BigDecimal amount() { return amount; }
    public String currency() { return currency; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrencyType() { return currency; }

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add Money with different currencies");
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot subtract Money with different currencies");
        }
        return new Money(this.amount.subtract(other.amount), this.currency);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount) && Objects.equals(currency, money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }
}

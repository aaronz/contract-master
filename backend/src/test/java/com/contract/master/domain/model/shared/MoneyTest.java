package com.contract.master.domain.model.shared;
import com.contract.master.shared.domain.model.TenantId;

import com.contract.master.shared.domain.model.Money;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MoneyTest {
    @Test
    void shouldCreateMoney() {
        Money money = new Money(new BigDecimal("100.00"), "CNY");
        assertThat(money.amount()).isEqualTo(new BigDecimal("100.00"));
        assertThat(money.currency()).isEqualTo("CNY");
    }

    @Test
    void shouldAddMoneyWithSameCurrency() {
        Money m1 = new Money(new BigDecimal("100.00"), "CNY");
        Money m2 = new Money(new BigDecimal("50.00"), "CNY");
        Money sum = m1.add(m2);
        assertThat(sum.amount()).isEqualTo(new BigDecimal("150.00"));
    }

    @Test
    void shouldThrowExceptionWhenAddingDifferentCurrencies() {
        Money m1 = new Money(new BigDecimal("100.00"), "CNY");
        Money m2 = new Money(new BigDecimal("50.00"), "USD");
        assertThrows(IllegalArgumentException.class, () -> m1.add(m2));
    }
}

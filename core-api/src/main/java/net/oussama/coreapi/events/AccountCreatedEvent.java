package net.oussama.coreapi.events;

import lombok.Getter;
import net.oussama.coreapi.enums.AccountStatus;

import java.math.BigDecimal;

public class AccountCreatedEvent extends BaseEvent<String> {
    @Getter
    private final BigDecimal initialBalance;
    @Getter
    private final String currency;
    @Getter
    private final AccountStatus status;

    public AccountCreatedEvent(String id, BigDecimal initialBalance, String currency, AccountStatus status) {
        super(id);
        this.initialBalance = initialBalance;
        this.currency = currency;
        this.status = status;
    }
}

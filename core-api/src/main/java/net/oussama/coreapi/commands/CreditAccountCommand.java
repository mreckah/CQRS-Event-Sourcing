package net.oussama.coreapi.commands;

import lombok.Getter;

import java.math.BigDecimal;

public class CreditAccountCommand extends BaseCommand<String> {
    @Getter
    private final BigDecimal amount;
    @Getter
    private final String currency;

    public CreditAccountCommand(String id, BigDecimal amount, String currency) {
        super(id);
        this.amount = amount;
        this.currency = currency;
    }
}

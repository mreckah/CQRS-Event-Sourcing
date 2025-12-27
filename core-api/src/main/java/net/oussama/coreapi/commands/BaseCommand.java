package net.oussama.coreapi.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import lombok.Getter;

import java.math.BigDecimal;

public class BaseCommand<T> {
    @TargetAggregateIdentifier
    @Getter
    private final T id;

    public BaseCommand(T id) {
        this.id = id;
    }
}

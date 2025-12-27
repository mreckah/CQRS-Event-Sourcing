package net.oussama.analytics.services;

import lombok.extern.slf4j.Slf4j;
import net.oussama.coreapi.events.AccountCreatedEvent;
import net.oussama.coreapi.events.AccountCreditedEvent;
import net.oussama.coreapi.events.AccountDebitedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class AnalyticsService {

    private final Map<String, BigDecimal> totalBalancePerCurrency = new HashMap<>();

    @KafkaListener(topics = "bank-events", groupId = "analytics-group")
    public void consume(Object event) {
        log.info("Analytics received event: {}", event);

        if (event instanceof AccountCreatedEvent e) {
            totalBalancePerCurrency.merge(e.getCurrency(), e.getInitialBalance(), BigDecimal::add);
        } else if (event instanceof AccountCreditedEvent e) {
            totalBalancePerCurrency.merge(e.getCurrency(), e.getAmount(), BigDecimal::add);
        } else if (event instanceof AccountDebitedEvent e) {
            totalBalancePerCurrency.merge(e.getCurrency(), e.getAmount().negate(), BigDecimal::add);
            // Beware of negative if balance < 0 logic isn't checked here,
            // but in Event Sourcing, events are facts.
        }

        log.info("Current Total Balances: {}", totalBalancePerCurrency);
    }
}

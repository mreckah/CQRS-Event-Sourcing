package net.oussama.analytics.services;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.oussama.analytics.model.AccountOperation;
import net.oussama.coreapi.events.AccountCreatedEvent;
import net.oussama.coreapi.events.AccountCreditedEvent;
import net.oussama.coreapi.events.AccountDebitedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Slf4j
public class AnalyticsService {

    private final Map<String, BigDecimal> totalBalancePerCurrency = new HashMap<>();

    @Getter
    private final Map<String, BigDecimal> accountBalances = new ConcurrentHashMap<>();

    @Getter
    private final Map<String, List<AccountOperation>> accountOperations = new ConcurrentHashMap<>();

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @KafkaListener(topics = "bank-events", groupId = "analytics-group")
    public void consume(Object event) {
        log.info("Analytics received event: {}", event);

        AccountOperation operation = null;

        if (event instanceof AccountCreatedEvent e) {
            totalBalancePerCurrency.merge(e.getCurrency(), e.getInitialBalance(), BigDecimal::add);
            accountBalances.put(e.getId(), e.getInitialBalance());

            operation = new AccountOperation(
                    e.getId(),
                    "CREATED",
                    e.getInitialBalance(),
                    e.getInitialBalance(),
                    e.getCurrency(),
                    LocalDateTime.now());

        } else if (event instanceof AccountCreditedEvent e) {
            totalBalancePerCurrency.merge(e.getCurrency(), e.getAmount(), BigDecimal::add);
            BigDecimal newBalance = accountBalances.merge(e.getId(), e.getAmount(), BigDecimal::add);

            operation = new AccountOperation(
                    e.getId(),
                    "CREDITED",
                    e.getAmount(),
                    newBalance,
                    e.getCurrency(),
                    LocalDateTime.now());

        } else if (event instanceof AccountDebitedEvent e) {
            totalBalancePerCurrency.merge(e.getCurrency(), e.getAmount().negate(), BigDecimal::add);
            BigDecimal newBalance = accountBalances.merge(e.getId(), e.getAmount().negate(), BigDecimal::add);

            operation = new AccountOperation(
                    e.getId(),
                    "DEBITED",
                    e.getAmount(),
                    newBalance,
                    e.getCurrency(),
                    LocalDateTime.now());
        }

        if (operation != null) {
            accountOperations.computeIfAbsent(operation.getAccountId(), k -> new ArrayList<>())
                    .add(operation);

            sendToAllEmitters(operation);
        }

        log.info("Current Total Balances: {}", totalBalancePerCurrency);
    }

    public void addEmitter(SseEmitter emitter) {
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(emitter));
    }

    private void sendToAllEmitters(AccountOperation operation) {
        List<SseEmitter> deadEmitters = new ArrayList<>();

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("account-operation")
                        .data(operation));
            } catch (Exception e) {
                deadEmitters.add(emitter);
            }
        }

        emitters.removeAll(deadEmitters);
    }

    public List<AccountOperation> getAccountHistory(String accountId) {
        return accountOperations.getOrDefault(accountId, Collections.emptyList());
    }
}

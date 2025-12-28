package net.oussama.analytics.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.oussama.analytics.model.AccountOperation;
import net.oussama.coreapi.events.AccountCreatedEvent;
import net.oussama.coreapi.events.AccountCreditedEvent;
import net.oussama.coreapi.events.AccountDebitedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalyticsService {

    private final Map<String, BigDecimal> totalBalancePerCurrency = new HashMap<>();

    private final ObjectMapper objectMapper;

    @Getter
    private final Map<String, BigDecimal> accountBalances = new ConcurrentHashMap<>();

    @Getter
    private final Map<String, List<AccountOperation>> accountOperations = new ConcurrentHashMap<>();

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    private static BigDecimal toBigDecimal(Object v) {
        if (v == null)
            return BigDecimal.ZERO;
        if (v instanceof BigDecimal bd)
            return bd;
        if (v instanceof Number n)
            return new BigDecimal(n.toString());
        if (v instanceof String s) {
            if (s.isBlank())
                return BigDecimal.ZERO;
            return new BigDecimal(s);
        }
        return new BigDecimal(String.valueOf(v));
    }

    @KafkaListener(topics = "bank-events")
    public void consume(@Header(KafkaHeaders.RECEIVED_KEY) String eventType, String eventJson) {
        log.info("Analytics received event (type={}): {}", eventType, eventJson);

        AccountOperation operation = null;

        try {
            Object event = objectMapper.readValue(eventJson, Object.class);
            if (event instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) event;
                String accountId = Objects.toString(map.get("id"), null);
                String currency = Objects.toString(map.get("currency"), null);
                if (accountId == null || accountId.isBlank()) {
                    log.warn("Received Map payload without 'id' field (type key='{}'): {}", eventType, map);
                    return;
                }

                if ("AccountCreatedEvent".equals(eventType)) {
                    BigDecimal initialBalance = toBigDecimal(map.get("initialBalance"));
                    if (currency == null || currency.isBlank())
                        currency = "";
                    totalBalancePerCurrency.merge(currency, initialBalance, BigDecimal::add);
                    accountBalances.put(accountId, initialBalance);
                    operation = new AccountOperation(accountId, "CREATED", initialBalance, initialBalance, currency,
                            LocalDateTime.now());
                } else if ("AccountCreditedEvent".equals(eventType)) {
                    BigDecimal amount = toBigDecimal(map.get("amount"));
                    if (currency == null || currency.isBlank())
                        currency = "";
                    totalBalancePerCurrency.merge(currency, amount, BigDecimal::add);
                    BigDecimal newBalance = accountBalances.merge(accountId, amount, BigDecimal::add);
                    operation = new AccountOperation(accountId, "CREDITED", amount, newBalance, currency,
                            LocalDateTime.now());
                } else if ("AccountDebitedEvent".equals(eventType)) {
                    BigDecimal amount = toBigDecimal(map.get("amount"));
                    if (currency == null || currency.isBlank())
                        currency = "";
                    totalBalancePerCurrency.merge(currency, amount.negate(), BigDecimal::add);
                    BigDecimal newBalance = accountBalances.merge(accountId, amount.negate(), BigDecimal::add);
                    operation = new AccountOperation(accountId, "DEBITED", amount, newBalance, currency,
                            LocalDateTime.now());
                } else {
                    log.debug("Ignoring unsupported event type key '{}'", eventType);
                    return;
                }
            } else if (event instanceof AccountCreatedEvent) {
                AccountCreatedEvent e = (AccountCreatedEvent) event;
                totalBalancePerCurrency.merge(e.getCurrency(), e.getInitialBalance(), BigDecimal::add);
                accountBalances.put(e.getId(), e.getInitialBalance());

                operation = new AccountOperation(
                        e.getId(),
                        "CREATED",
                        e.getInitialBalance(),
                        e.getInitialBalance(),
                        e.getCurrency(),
                        LocalDateTime.now());
            } else if (event instanceof AccountCreditedEvent) {
                AccountCreditedEvent e = (AccountCreditedEvent) event;
                totalBalancePerCurrency.merge(e.getCurrency(), e.getAmount(), BigDecimal::add);
                BigDecimal newBalance = accountBalances.merge(e.getId(), e.getAmount(), BigDecimal::add);

                operation = new AccountOperation(
                        e.getId(),
                        "CREDITED",
                        e.getAmount(),
                        newBalance,
                        e.getCurrency(),
                        LocalDateTime.now());
            } else if (event instanceof AccountDebitedEvent) {
                AccountDebitedEvent e = (AccountDebitedEvent) event;
                totalBalancePerCurrency.merge(e.getCurrency(), e.getAmount().negate(), BigDecimal::add);
                BigDecimal newBalance = accountBalances.merge(e.getId(), e.getAmount().negate(), BigDecimal::add);

                operation = new AccountOperation(
                        e.getId(),
                        "DEBITED",
                        e.getAmount(),
                        newBalance,
                        e.getCurrency(),
                        LocalDateTime.now());
            } else {
                log.debug("Ignoring unsupported event type '{}'", event.getClass().getName());
                return;
            }
        } catch (Exception e) {
            log.error("Error processing Kafka message: {}", e.getMessage(), e);
            return;
        }

        if (operation != null) {
            accountOperations.computeIfAbsent(operation.getAccountId(), k -> new ArrayList<>())
                    .add(operation);

            sendToAllEmitters(operation);
            log.info("Current Total Balances: {}", totalBalancePerCurrency);
        }
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

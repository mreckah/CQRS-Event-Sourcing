package net.oussama.accountservice.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.oussama.coreapi.events.BaseEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class KafkaEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @EventHandler
    public void on(Object event) {
        // Simple bridge: publish all domain events to a single topic
        if (event.getClass().getPackageName().startsWith("net.youssfi.coreapi.events")) {
            log.info("Publishing event to Kafka: {}", event.getClass().getSimpleName());
            kafkaTemplate.send("bank-events", event.getClass().getSimpleName(), event);
        }
    }
}

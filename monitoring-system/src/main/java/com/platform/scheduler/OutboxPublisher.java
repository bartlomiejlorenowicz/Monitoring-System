package com.platform.scheduler;

import com.platform.model.OutboxEvent;
import com.platform.model.OutboxStatus;
import com.platform.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OutboxPublisher {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 3000)
    @Transactional
    public void publishEvents() {
        List<OutboxEvent> events = outboxRepository.findBatchForProcessing(OutboxStatus.NEW.name(), 10);

        for (OutboxEvent event : events) {
            try {
                event.setStatus(OutboxStatus.PROCESSING);
                outboxRepository.save(event);

                kafkaTemplate.send(
                                event.getTopic(),
                                event.getEventId(),
                                event.getPayload())
                        .get();

                event.setStatus(OutboxStatus.PUBLISHED);
                event.setPublishedAt(Instant.now());
                event.setErrorMessage(null);

                log.info("Published outbox event id={}, eventId={}", event.getId(), event.getEventId());
            } catch (Exception e) {
                event.setStatus(OutboxStatus.FAILED);
                event.setRetryCount(event.getRetryCount() + 1);
                event.setErrorMessage(truncate(e.getMessage(), 500));
                outboxRepository.save(event);

                log.error("Failed to publish outbox event id={}", event.getId(), e);
            }
        }
    }

    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void requeueFailedEvents() {
        List<OutboxEvent> failedEvents = outboxRepository.findTop10ByStatusOrderByCreatedAtAsc(OutboxStatus.FAILED);

        for (OutboxEvent event : failedEvents) {
            if (event.getRetryCount() < 5) {
                event.setStatus(OutboxStatus.NEW);
            }
        }
    }

    private String truncate(String value, int max) {
        if (value == null) {
            return null;
        }
        return value.length() <= max ? value : value.substring(0, max);
    }
}
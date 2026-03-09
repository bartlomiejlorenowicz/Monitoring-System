package com.platform.scheduler;

import com.platform.model.OutboxEvent;
import com.platform.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OutboxPublisher {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 5000)
    public void publishEvent() {
        System.out.println("Publishing event");

        List<OutboxEvent> events = outboxRepository.findByPublishedFalse();

        for (OutboxEvent event : events) {
            kafkaTemplate.send(event.getTopic(), event.getPayload());

            event.setPublished(true);
            outboxRepository.save(event);
        }
    }
}

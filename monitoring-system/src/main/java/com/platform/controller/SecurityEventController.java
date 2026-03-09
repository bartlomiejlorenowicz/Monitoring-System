package com.platform.controller;

import com.platform.kafka.producer.SecurityEventProducer;
import com.platform.model.SecurityEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/events")
public class SecurityEventController {

    private final SecurityEventProducer producer;

    @PostMapping
    public String SendEvent(@RequestBody SecurityEvent event) {
        producer.sendEvent(event);

        return "Event sent to Kafka";
    }
}

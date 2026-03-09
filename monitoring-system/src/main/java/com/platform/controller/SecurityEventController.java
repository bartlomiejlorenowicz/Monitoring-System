package com.platform.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.platform.dto.SecurityEventMessage;
import com.platform.kafka.producer.SecurityEventProducer;
import com.platform.service.SecurityEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/events")
public class SecurityEventController {

    private final SecurityEventService service;

    @PostMapping
    public String sendEvent(@RequestBody SecurityEventMessage request) throws JsonProcessingException {
        service.saveEvent(request);

        return "Event sent to Kafka";
    }
}

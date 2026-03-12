package com.platform.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.platform.events.SecurityEventMessage;
import com.platform.service.SecurityEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/events")
public class SecurityEventController {

    private final SecurityEventService service;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String sendEvent(@Valid @RequestBody SecurityEventMessage request) throws JsonProcessingException {
        service.saveEvent(request);
        return "Event accepted for processing";
    }
}

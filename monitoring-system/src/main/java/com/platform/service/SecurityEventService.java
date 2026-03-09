package com.platform.service;

import com.platform.model.SecurityEvent;
import com.platform.repository.SecurityEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityEventService {

    private final SecurityEventRepository repository;

    public void saveEvent(SecurityEvent event) {
        repository.save(event);
    }
}

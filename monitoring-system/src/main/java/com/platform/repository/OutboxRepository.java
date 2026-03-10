package com.platform.repository;

import com.platform.model.OutboxEvent;
import com.platform.model.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OutboxRepository extends JpaRepository<OutboxEvent, Long> {

    Optional<OutboxEvent> findByEventId(String eventId);

    List<OutboxEvent> findTop10ByStatusOrderByCreatedAtAsc(OutboxStatus outboxStatus);
}

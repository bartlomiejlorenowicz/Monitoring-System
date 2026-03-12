package com.platform.repository;

import com.platform.model.OutboxEvent;
import com.platform.model.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OutboxRepository extends JpaRepository<OutboxEvent, Long> {

    boolean existsByEventId(String eventId);

    List<OutboxEvent> findTop10ByStatusOrderByCreatedAtAsc(OutboxStatus outboxStatus);

    @Query(value = """
        SELECT *
        FROM outbox_events
        WHERE status = :status
        ORDER BY created_at
        LIMIT :limit
        FOR UPDATE SKIP LOCKED
        """, nativeQuery = true)
    List<OutboxEvent> findBatchForProcessing(
            @Param("status") String status,
            @Param("limit") int limit
    );

    @Modifying
    @Query(value = """
        UPDATE outbox_events
        SET status = 'NEW',
            processing_started_at = NULL
        WHERE status = 'PROCESSING'
        AND processing_started_at < now() - interval '5 minutes'
        """, nativeQuery = true)
    int recoverStuckEvents();
}

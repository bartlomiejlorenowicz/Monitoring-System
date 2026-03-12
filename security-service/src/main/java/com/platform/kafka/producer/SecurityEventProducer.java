//package com.platform.kafka.producer;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.platform.dto.SecurityEventMessage;
//import lombok.RequiredArgsConstructor;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//@RequiredArgsConstructor
//@Service
//public class SecurityEventProducer {
//
//    private final KafkaTemplate<String, SecurityEventMessage> kafkaTemplate;
//    private final ObjectMapper objectMapper;
//
//    public void sendEvent(SecurityEventMessage event) throws JsonProcessingException {
//        String payload = objectMapper.writeValueAsString(event);
//        kafkaTemplate.send("security-events", payload);
//    }
//}

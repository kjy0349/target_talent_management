package com.ssafy.s10p31s102be.admin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.s10p31s102be.admin.dto.request.NotificationAdminCreateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaNotificationProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    public void sendNotificationToKafkaTopic(NotificationAdminCreateDto notificationRequest) {
        System.out.println("send to kafka ");
        String topicName = "notification";
        String payload = "";
        try {
            payload = objectMapper.writeValueAsString(notificationRequest);
        } catch (JsonProcessingException e) {
            log.info(e.getMessage());
            throw new RuntimeException("Object Mapping에 실패했습니다.");
        }
        kafkaTemplate.send(topicName, payload);
    }
}

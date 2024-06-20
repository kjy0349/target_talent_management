package com.ssafy.s10p31s102be.admin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.s10p31s102be.admin.dto.request.NotificationAdminCreateDto;
import com.ssafy.s10p31s102be.admin.dto.request.NotificationAdminMemberCreateDto;
import com.ssafy.s10p31s102be.admin.dto.response.NotificationFullDto;
import com.ssafy.s10p31s102be.admin.infra.entity.Notification;
import com.ssafy.s10p31s102be.admin.infra.entity.NotificationData;
import com.ssafy.s10p31s102be.admin.infra.repository.NotificationJpaRepository;
import com.ssafy.s10p31s102be.member.exception.MemberNotFoundException;
import com.ssafy.s10p31s102be.member.infra.repository.MemberJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ssafy.s10p31s102be.admin.infra.entity.QNotification.notification;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaNotificationConsumerService {
    private final ObjectMapper objectMapper;
    private final MemberJpaRepository memberRepository;
    private final NotificationJpaRepository notificationRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Transactional
    @KafkaListener(topics = "notification", groupId = "notificationGroup")
    public void consume(ConsumerRecord<String, String> message) {
        log.info("consume On ");
        try {

            log.info("Consume Messasge : " + message.value());
            NotificationAdminCreateDto notificationRequest = objectMapper.readValue(message.value(), NotificationAdminCreateDto.class);
//            List<NotificationData> dataList = notificationRequest.getData().stream().map( id -> NotificationData.from( id )).toList();
            List<NotificationData> dataList = new ArrayList<>();
            System.out.println("datas : " + notificationRequest.getData() );
            if( notificationRequest.getData() != null ){
                dataList = notificationRequest.getData().stream().map(NotificationData::from).toList();
            }
            Notification notification = Notification.builder()
                    .member(memberRepository.findById( notificationRequest.getMember().getMemberId() )
                            .orElseThrow(() -> new MemberNotFoundException(notificationRequest.getMember().getMemberId(), this)))
                    .content(notificationRequest.getContent())
                    .isRead(false)
                    .notificationType(notificationRequest.getNotificationType())
                    .notificationData(dataList)
                    .notificationDataType(notificationRequest.getNotificationDataType())
                    .senderName(notificationRequest.getSenderName())
                    .build();
            notification.getNotificationData().forEach( nd -> nd.setNotification( notification ));
            System.out.println("save = " + notification.getContent() + " " + notification.getNotificationData().size());
            notificationRepository.save(notification);
            NotificationFullDto dto = NotificationFullDto.from( notification );
            System.out.println("sendurl: "+ "/all-notification/" + dto.getMember().getId());
            simpMessagingTemplate.convertAndSend("/all-notification/" + dto.getMember().getId(), objectMapper.writeValueAsString(dto));
        }catch (Exception e) {
            log.info(e.getMessage());
            throw new RuntimeException("Object Mapping에 실패했습니다.");
        }
    }
}

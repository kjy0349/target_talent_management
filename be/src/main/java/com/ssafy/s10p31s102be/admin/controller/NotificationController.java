package com.ssafy.s10p31s102be.admin.controller;

import com.ssafy.s10p31s102be.admin.dto.request.NotificationAdminCreateDto;
import com.ssafy.s10p31s102be.admin.dto.request.SystemNotificationCreateDto;
import com.ssafy.s10p31s102be.admin.dto.response.SystemNotificationFullDto;
import com.ssafy.s10p31s102be.admin.dto.request.SystemNotificationUpdateDto;
import com.ssafy.s10p31s102be.admin.dto.response.NotificationFullDto;
import com.ssafy.s10p31s102be.admin.infra.entity.Notification;
import com.ssafy.s10p31s102be.admin.service.AdminScheduledService;
import com.ssafy.s10p31s102be.admin.service.KafkaNotificationProducerService;
import com.ssafy.s10p31s102be.admin.service.NotificationService;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "알림")
@RequiredArgsConstructor
@RestController
public class NotificationController {
    private final AdminScheduledService adminScheduledService;
    private final KafkaNotificationProducerService kafkaNotificationProducerService;
    private final NotificationService notificationService;
    @Operation(summary = "알림 카프카 토픽 내 메시지 발행", description = "카프카에 새로운 알림을 생성하는 로직")
    @MessageMapping("/notification")
    public ResponseEntity<Void> sendNotificationToMember(NotificationAdminCreateDto notificationRequest) {
        kafkaNotificationProducerService.sendNotificationToKafkaTopic(notificationRequest);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "스케쥴링 알림 발행", description = "카프카에 스케쥴링 알림을 발행하는 로직")
    @PostMapping("/notification/schedule")
    public ResponseEntity<Void> fetchExternalScheduledJob( @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody SystemNotificationCreateDto dto ){
        System.out.println("message arrived, " + dto.toString());
        adminScheduledService.fetch( dto );
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "알림 조회", description = "인가정보에 따른 알림 리스트 조회 로직")
    @GetMapping("/notification")
    public ResponseEntity<List<NotificationFullDto>> getPreviousNotificationsByAuthentication(@AuthenticationPrincipal UserDetailsImpl userDetails){
        List<Notification> notifications = notificationService.getAllNotificationsByAuthentication( userDetails );
        return ResponseEntity.ok( notifications.stream().map( notification -> NotificationFullDto.from( notification ) ).toList() );
    }

    @Operation(summary = "특정 회원 알림 수정", description = "인가 사용자에 대한 알림 수정 로직 -> 읽기에 사용")
    @PutMapping("/notification/{notificationId}")
    public ResponseEntity<Void> readNotificationByMember(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "notificationId") Integer notificationId){
        notificationService.readNotificationByMember( userDetails, notificationId );
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "스케쥴링 알림 생성", description = "스케쥴링 알림을 새로 생성하는 로직")
    @PostMapping("/notification/system")
    public ResponseEntity<Void> createSystemNotification( @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody SystemNotificationCreateDto dto ){
        notificationService.createSystemNotification( userDetails, dto );
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "스케쥴링 알림 조회 로직", description = "Python Fast api에서 스케쥴링 알림을 조회하는 로직")
    @GetMapping("/notification/system")
    public ResponseEntity<List<SystemNotificationFullDto>> getAllSystemNotifications(@AuthenticationPrincipal UserDetailsImpl userDetails){
        List<SystemNotificationFullDto> dtoList = notificationService.getAllSystemNotificationsByAuthentication( userDetails ).stream().map( SystemNotificationFullDto::fromEntity).toList();
        return ResponseEntity.ok( dtoList );
    }
    @Operation(summary = "스케쥴링 알림 수정", description = "스케쥴링 알림을 수정하는 로직")
    @PutMapping("/notification/system/{systemNotificationId}")
    public ResponseEntity<Void> updateSystemNotification(@AuthenticationPrincipal UserDetailsImpl userDetails,@PathVariable(value = "systemNotificationId") Integer systemNotificationId ,@RequestBody SystemNotificationUpdateDto dto ){
        notificationService.updateSystemNotfication( userDetails, systemNotificationId, dto );
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "스케쥴링 알림 삭제", description = "스케쥴링 알림을 삭제하는 로직")
    @DeleteMapping("/notification/system/{systemNotificationId}")
    public ResponseEntity<Void> deleteSystemNotification(@AuthenticationPrincipal UserDetailsImpl userDetails,@PathVariable(value = "systemNotificationId") Integer systemNotificationId ){
        notificationService.deleteSystemNotfication( userDetails, systemNotificationId );
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "네트워킹 알림 조회", description = "인가정보에 따른 자신의 네트워킹 알림 조회 로직")
    @GetMapping("/notification/networking")
    public ResponseEntity<List<NotificationFullDto>> getAllNetworkingNotification( @AuthenticationPrincipal UserDetailsImpl userDetails ){
        List<Notification> notifications = notificationService.getAllNetworkingNotification( userDetails );
        return ResponseEntity.ok( notifications.stream().map( notification -> NotificationFullDto.from( notification ) ).toList() );
    }
}

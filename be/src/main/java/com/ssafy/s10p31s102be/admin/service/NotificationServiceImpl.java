package com.ssafy.s10p31s102be.admin.service;

import com.ssafy.s10p31s102be.admin.dto.request.SystemNotificationCreateDto;
import com.ssafy.s10p31s102be.admin.dto.request.SystemNotificationUpdateDto;
import com.ssafy.s10p31s102be.admin.infra.entity.Notification;
import com.ssafy.s10p31s102be.admin.infra.entity.NotificationDataType;
import com.ssafy.s10p31s102be.admin.infra.entity.SystemNotification;
import com.ssafy.s10p31s102be.admin.infra.enums.NotificationType;
import com.ssafy.s10p31s102be.admin.infra.repository.NotificationJpaRepository;
import com.ssafy.s10p31s102be.admin.infra.repository.SystemNotificaitonJpaRepository;
import com.ssafy.s10p31s102be.admin.utils.ScheduleWebClient;
import com.ssafy.s10p31s102be.common.exception.InvalidAuthorizationException;
import com.ssafy.s10p31s102be.common.exception.NotificationNotFoundException;
import com.ssafy.s10p31s102be.common.exception.SystemNotificationNotFoundException;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService{
    private final NotificationJpaRepository notificationJpaRepository;
    private final SystemNotificaitonJpaRepository systemNotificaitonRepository;
    private final ScheduleWebClient scheduleWebClient;

    @Override
    public List<Notification> getAllNotificationsByAuthentication(UserDetailsImpl userDetails) {
        if( userDetails == null ){
            return new ArrayList<>();
        }
        return notificationJpaRepository.getAllNotificationsByAuthentication( userDetails.getMemberId());
    }

    @Override
    public void readNotificationByMember(UserDetailsImpl userDetails, Integer notificationId) {
        Notification notification = notificationJpaRepository.findById( notificationId )
                .orElseThrow( () -> new NotificationNotFoundException( notificationId, this));

        notification.updateRead();

    }

    @Override
    public SystemNotification createSystemNotification(UserDetailsImpl userDetails, SystemNotificationCreateDto dto) {
        if( !userDetails.getAuthorityLevel().equals(1) ){
            throw new InvalidAuthorizationException( userDetails.getMemberId(),this );
        }
        Integer maxIdx = systemNotificaitonRepository.getMaxIdx() + 1;
        SystemNotification systemNotification =
                SystemNotification.builder()
                        .title(dto.getTitle())
                        .content(dto.getContent())
                        .idx(maxIdx)
                        .isActive(true)
                        .period(dto.getPeriod())
                        .calculateWeek(dto.getCalculateWeek())
                        .lastSendedAt(LocalDateTime.now())
                        .build();
        systemNotification.setModifiedAt(LocalDateTime.now());
        systemNotificaitonRepository.save( systemNotification );
        Mono<String> result = scheduleWebClient.addNotification( systemNotification );
        System.out.println( result.block());
        return systemNotification;
    }

    @Override
    public List<SystemNotification> getAllSystemNotificationsByAuthentication(UserDetailsImpl userDetails) {
        List<SystemNotification> systemNotifications = systemNotificaitonRepository.findAll();
        return systemNotifications;
    }

    @Override
    public SystemNotification updateSystemNotfication(UserDetailsImpl userDetails, Integer systemNotificationId, SystemNotificationUpdateDto dto) {
        SystemNotification systemNotification = systemNotificaitonRepository.findById( systemNotificationId )
                .orElseThrow(()-> new SystemNotificationNotFoundException( systemNotificationId,this));
        if( !dto.getIsActive() && ( systemNotification.getIsActive() != dto.getIsActive() ) ){
            systemNotification.update( dto );
            Mono<String> result = scheduleWebClient.deleteNotification( systemNotificationId );
            System.out.println(result.block());
        }else if( dto.getIsActive() && ( dto.getIsActive() != systemNotification.getIsActive() ) ){
            systemNotification.update( dto );
            Mono<String> result = scheduleWebClient.addNotification( systemNotification );
            System.out.println(result.block());
        }else{
            systemNotification.update( dto );
            Mono<String> result = scheduleWebClient.putNotification( systemNotificationId, dto.getPeriod() );
            System.out.println( result.block());
        }

        return systemNotification;
    }

    @Override
    public void deleteSystemNotfication(UserDetailsImpl userDetails, Integer systemNotificationId) {
        Mono<String> result = scheduleWebClient.deleteNotification( systemNotificationId );
        System.out.println( result.block());
        systemNotificaitonRepository.deleteNotificationById( systemNotificationId );
    }

    @Override
    public List<Notification> getAllNetworkingNotification(UserDetailsImpl userDetails) {
        return notificationJpaRepository.getAllPersonalNotification( NotificationType.NETWOKRING_APPROAVE );
    }
}

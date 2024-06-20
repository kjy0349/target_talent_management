package com.ssafy.s10p31s102be.admin.service;

import com.ssafy.s10p31s102be.admin.dto.request.SystemNotificationCreateDto;
import com.ssafy.s10p31s102be.admin.dto.request.SystemNotificationUpdateDto;
import com.ssafy.s10p31s102be.admin.infra.entity.Notification;
import com.ssafy.s10p31s102be.admin.infra.entity.SystemNotification;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;

import java.util.List;

public interface NotificationService {
    List<Notification> getAllNotificationsByAuthentication(UserDetailsImpl userDetails);

    void readNotificationByMember(UserDetailsImpl userDetails, Integer notificationId);

    SystemNotification createSystemNotification(UserDetailsImpl userDetails, SystemNotificationCreateDto dto);

    List<SystemNotification> getAllSystemNotificationsByAuthentication(UserDetailsImpl userDetails);

    SystemNotification updateSystemNotfication(UserDetailsImpl userDetails, Integer systemNotificationId, SystemNotificationUpdateDto dto);

    void deleteSystemNotfication(UserDetailsImpl userDetails, Integer systemNotificationId);

    List<Notification> getAllNetworkingNotification(UserDetailsImpl userDetails);
}

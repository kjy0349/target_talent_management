package com.ssafy.s10p31s102be.admin.dto.response;

import com.ssafy.s10p31s102be.admin.infra.entity.Notification;
import com.ssafy.s10p31s102be.admin.infra.entity.NotificationData;
import com.ssafy.s10p31s102be.admin.infra.entity.NotificationDataType;
import com.ssafy.s10p31s102be.admin.infra.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationFullDto {

    private Integer id;
    private String content;
    private NotificationType notificationType;
    private NotificationDataType notificationDataType;
    private List<NotificationData> notificationData;
    private Boolean isRead;
    private MemberAdminFullDto member;
    private LocalDateTime createdAt;
    private String senderName;

    public static NotificationFullDto from(Notification notification ){

        return NotificationFullDto
                .builder()
                .id( notification.getId() )
                .content( notification.getContent() )
                .isRead( notification.getIsRead() )
                .member( MemberAdminFullDto.fromEntity(notification.getMember()))
                .notificationDataType(notification.getNotificationDataType())
                .notificationType(notification.getNotificationType())
                .notificationData( notification.getNotificationData().isEmpty() ? new ArrayList<>() : notification.getNotificationData().stream().toList() )
                .createdAt(notification.getCreatedAt())
                .senderName(notification.getSenderName())
                .build();
    }

}

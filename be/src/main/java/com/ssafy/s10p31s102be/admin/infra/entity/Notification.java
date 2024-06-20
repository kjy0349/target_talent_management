package com.ssafy.s10p31s102be.admin.infra.entity;

import com.ssafy.s10p31s102be.admin.infra.enums.NotificationType;
import com.ssafy.s10p31s102be.common.infra.entity.BaseTimeEntity;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Integer id;

    @Builder
    public Notification(String content, NotificationType notificationType, NotificationDataType notificationDataType, List<NotificationData> notificationData, Boolean isRead, Member member, String senderName) {
        this.content = content;
        this.notificationType = notificationType;
        this.notificationDataType = notificationDataType;
        this.notificationData = notificationData;
        this.isRead = isRead;
        this.member = member;
        this.senderName = senderName;
    }

    private String content;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Enumerated(EnumType.STRING)
    private NotificationDataType notificationDataType;

    @OneToMany( mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotificationData> notificationData;

    private Boolean isRead;
    @ManyToOne
    @JoinColumn( name = "member_id")
    private Member member;

    private String senderName;

    public void updateRead() {
        this.isRead = true;
    }
}

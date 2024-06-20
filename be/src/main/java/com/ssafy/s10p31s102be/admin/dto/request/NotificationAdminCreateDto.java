package com.ssafy.s10p31s102be.admin.dto.request;

import com.ssafy.s10p31s102be.admin.infra.entity.NotificationDataType;
import com.ssafy.s10p31s102be.admin.infra.enums.NotificationType;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationAdminCreateDto {

//    private Integer id;

    private String content;

    private NotificationType notificationType;

    private NotificationAdminMemberCreateDto member;

    private NotificationDataType notificationDataType;

    private List<Integer> data;

    private String senderName;

}

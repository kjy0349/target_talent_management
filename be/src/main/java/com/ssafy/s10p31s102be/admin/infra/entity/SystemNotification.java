package com.ssafy.s10p31s102be.admin.infra.entity;

import com.ssafy.s10p31s102be.admin.dto.request.SystemNotificationUpdateDto;
import com.ssafy.s10p31s102be.common.infra.entity.BaseTimeEntity;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemNotification extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Integer id;

    private String title;

    private String content;

    private Integer idx;

    private LocalDateTime lastSendedAt;

    private Integer calculateWeek;

    private Integer period;

    private Boolean isActive;


    public void update(SystemNotificationUpdateDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.calculateWeek = dto.getCalculateWeek();
        this.period = dto.getPeriod();
        this.isActive = dto.getIsActive();
    }

    public void updateSendedAt(){
        this.lastSendedAt = LocalDateTime.now();
    }
}

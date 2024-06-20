package com.ssafy.s10p31s102be.admin.dto.response;

import com.ssafy.s10p31s102be.admin.infra.entity.SystemNotification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemNotificationFullDto {
    private Integer id;

    private String title;

    private String content;

    private Integer idx;

    private LocalDateTime lastSendedAt;

    private Integer calculateWeek;

    private Integer period;

    private Boolean isActive;
    public static SystemNotificationFullDto fromEntity(SystemNotification systemNotification) {
        return SystemNotificationFullDto.builder()
                .id(systemNotification.getId())
                .content(systemNotification.getContent())
                .idx(systemNotification.getIdx())
                .lastSendedAt(systemNotification.getLastSendedAt())
                .calculateWeek(systemNotification.getCalculateWeek())
                .period(systemNotification.getPeriod())
                .isActive(systemNotification.getIsActive())
                .title(systemNotification.getTitle())
                .build();
    }
}

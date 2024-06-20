package com.ssafy.s10p31s102be.admin.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemNotificationCreateDto {
    private String content;
    private String title;
    private Integer idx;

    private LocalDateTime lastSendedAt;

    private Integer calculateWeek;

    private Integer period;

    private Boolean isActive;
}

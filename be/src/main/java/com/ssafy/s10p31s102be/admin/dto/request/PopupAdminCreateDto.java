package com.ssafy.s10p31s102be.admin.dto.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PopupAdminCreateDto {
    private Boolean isUsed;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer authId;
}

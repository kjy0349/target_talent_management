package com.ssafy.s10p31s102be.admin.dto.response;

import com.ssafy.s10p31s102be.admin.infra.entity.PopupMessage;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PopupMessageReadDto {
    private Integer id;
    private Boolean isUsed;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer authLevel;

    public static PopupMessageReadDto fromEntity(PopupMessage popupMessage){
        return PopupMessageReadDto.builder()
                .id(popupMessage.getId())
                .isUsed(popupMessage.getIsUsed())
                .content(popupMessage.getContent())
                .startDate(popupMessage.getStartDate())
                .endDate(popupMessage.getEndDate())
                .authLevel(popupMessage.getViewAuthority().getAuthLevel())
                .build();
    }
}

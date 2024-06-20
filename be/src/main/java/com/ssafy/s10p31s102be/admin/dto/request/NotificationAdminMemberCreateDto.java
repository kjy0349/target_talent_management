package com.ssafy.s10p31s102be.admin.dto.request;

import com.ssafy.s10p31s102be.member.infra.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationAdminMemberCreateDto {
    private Integer memberId;
    private String memberName;

    public static NotificationAdminMemberCreateDto fromEntity(Member member ){
        return NotificationAdminMemberCreateDto.builder()
                .memberId( member.getId())
                .memberName(member.getName())
                .build();
    }
}

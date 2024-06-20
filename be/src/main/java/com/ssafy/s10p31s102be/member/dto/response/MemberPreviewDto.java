package com.ssafy.s10p31s102be.member.dto.response;

import com.ssafy.s10p31s102be.member.infra.entity.Member;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberPreviewDto {
    private String name;
    private String departmentName;

    public static MemberPreviewDto fromEntity(Member member) {
        if (member == null) return null;
        return MemberPreviewDto.builder()
                .name(member.getName())
                .departmentName(member.getDepartment().getName())
                .build();
    }
}

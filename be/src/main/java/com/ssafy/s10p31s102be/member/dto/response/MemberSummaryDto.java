package com.ssafy.s10p31s102be.member.dto.response;

import com.ssafy.s10p31s102be.member.infra.entity.Member;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class MemberSummaryDto {
    private Integer id;
    private String name;
    private String departmentName;
    private String teamName;
    private String authority;
    private Boolean isSecuritySigned;
    private Boolean changePassword;
    private Integer authLevel;

    public static MemberSummaryDto fromEntity(Member member) {
        return MemberSummaryDto.builder()
                .id(member.getId())
                .name(member.getName())
                .departmentName(member.getDepartment().getName())
                .teamName(member.getTeam().getName())
                .authority(member.getAuthority().getAuthName())
                .isSecuritySigned(member.getIsSecuritySigned())
                .changePassword(member.getMustChangePassword())
                .authLevel(member.getAuthority().getAuthLevel())
                .build();
    }
}

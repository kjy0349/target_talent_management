package com.ssafy.s10p31s102be.admin.dto.response;

import com.ssafy.s10p31s102be.member.infra.entity.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberAdminFullDto {
    private Integer id;

    private String name;

    private String profileImage;

    private String departmentName;

    private String teamName;

    private String roleName;

    private String knoxId;

    private String authority;

    private LocalDateTime createdAt;

    private Integer visitCount;

    private LocalDateTime lastAccessDate;

    private boolean isSecuritySigned;

    private LocalDateTime lastDeletedAt;

    public static MemberAdminFullDto fromEntity(Member member){
        return MemberAdminFullDto.builder()
                .id(member.getId())
                .name( member.getName())
                .departmentName( member.getDepartment() == null ? "사업부 없음":member.getDepartment().getName() )
                .teamName(member.getTeam() == null ? "부서 없음" : member.getTeam().getName())
                .knoxId(member.getKnoxId())
                .visitCount(member.getVisitCount())
                .lastAccessDate(member.getLastAccessDate())
                .createdAt(member.getCreatedAt())
                .authority( member.getAuthority() == null ? "권한 없음": member.getAuthority().getAuthName())
                .roleName( member.getRole() != null ? member.getRole().getDescription() : "담당 업무 없음")
                .isSecuritySigned( member.getIsSecuritySigned() == null ? false : member.getIsSecuritySigned() )
                .lastDeletedAt(member.getLastDeletedAt())
                .profileImage(member.getProfileImage())
                .build();
    }

}

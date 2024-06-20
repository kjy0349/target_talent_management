package com.ssafy.s10p31s102be.admin.dto.response;

import com.ssafy.s10p31s102be.member.infra.entity.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberAdminSummaryDto {
    private Integer id;

    private String name;

    private String profileImage;
    
    private String departmentName;
    
    private String knoxId;

    private String telephone;

    private Integer visitCount;
    
    private LocalDateTime lastAccessDate;

    private LocalDateTime createdAt;

    private Boolean isSecuritySigned;

    private Integer managePoolSize;

    private Integer completedNetworks;


    public static MemberAdminSummaryDto fromEntity(Member member, Integer managePoolSize, Integer completedNetworks ){
        return MemberAdminSummaryDto.builder()
                .id(member.getId())
                .name( member.getName())
                .departmentName( member.getDepartment() == null ? "부서 없음":member.getDepartment().getName() )
                .telephone(member.getTelephone())
                .knoxId(member.getKnoxId())
                .visitCount(member.getVisitCount())
                .lastAccessDate(member.getLastAccessDate())
                .createdAt(member.getCreatedAt())
                .isSecuritySigned(member.getIsSecuritySigned())
                .managePoolSize(managePoolSize)
                .completedNetworks(completedNetworks)
                .profileImage(member.getProfileImage())
                .build();
    }

}

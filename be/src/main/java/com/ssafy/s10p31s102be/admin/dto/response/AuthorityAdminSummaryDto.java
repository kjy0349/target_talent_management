package com.ssafy.s10p31s102be.admin.dto.response;

import com.ssafy.s10p31s102be.member.infra.entity.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorityAdminSummaryDto {
    private Integer id;
    private String authName;
    private Integer authLevel;
    private String authDescription;
    public static AuthorityAdminSummaryDto fromEntity(Authority authority ){
        AuthorityAdminSummaryDto dto = new AuthorityAdminSummaryDto();
        dto.id = authority.getId();
        dto.authName = authority.getAuthName();
        dto.authLevel = authority.getAuthLevel();
        dto.authDescription = authority.getAuthDescription();
        return dto;
    }
}

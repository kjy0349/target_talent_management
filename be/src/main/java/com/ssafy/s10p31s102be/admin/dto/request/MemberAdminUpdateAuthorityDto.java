package com.ssafy.s10p31s102be.admin.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberAdminUpdateAuthorityDto {
    private List<Integer> memberIds;
    private Integer authorityId;
}

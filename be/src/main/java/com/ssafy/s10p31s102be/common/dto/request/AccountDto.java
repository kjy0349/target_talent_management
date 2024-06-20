package com.ssafy.s10p31s102be.common.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private String name;
    private String password;
    private String authorityName;
    private Integer memberId;
    private Integer authorityLevel;
    private Integer departmentId;
    private Integer teamId;
}

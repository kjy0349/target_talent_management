package com.ssafy.s10p31s102be.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberAuthorizationDto {
    private String knoxId;
    private String authorizationCode;
}

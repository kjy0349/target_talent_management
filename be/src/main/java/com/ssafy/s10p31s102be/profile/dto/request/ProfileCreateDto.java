package com.ssafy.s10p31s102be.profile.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileCreateDto {
    private String profileImage;
    private Boolean isPrivate;
    private Boolean isAllCompany;
    private Integer targetJobRankId;
    private Map<String, String> columns;
}

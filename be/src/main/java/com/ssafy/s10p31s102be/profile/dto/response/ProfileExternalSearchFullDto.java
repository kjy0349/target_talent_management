package com.ssafy.s10p31s102be.profile.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileExternalSearchFullDto {
    private List<ProfilePreviewDto> profileList;
    private Long maxCount;
    private Integer maxPage;
}

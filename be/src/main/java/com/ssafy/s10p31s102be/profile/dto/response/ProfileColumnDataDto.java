package com.ssafy.s10p31s102be.profile.dto.response;

import com.ssafy.s10p31s102be.profile.infra.entity.ProfileColumnData;
import com.ssafy.s10p31s102be.profile.infra.enums.ProfileColumnDataType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileColumnDataDto {
    private String name;
    private String label;
    private String value;
    private ProfileColumnDataType dataType;

    public static ProfileColumnDataDto fromEntity(ProfileColumnData profileColumnData) {
        return ProfileColumnDataDto.builder()
                .name(profileColumnData.getProfileColumn().getName())
                .label(profileColumnData.getProfileColumn().getLabel())
                .value(profileColumnData.getContent())
                .dataType(profileColumnData.getProfileColumn().getDataType())
                .build();
    }
}

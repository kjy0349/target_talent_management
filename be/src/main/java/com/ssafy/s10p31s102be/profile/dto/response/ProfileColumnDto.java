package com.ssafy.s10p31s102be.profile.dto.response;

import com.ssafy.s10p31s102be.profile.infra.entity.ProfileColumn;
import com.ssafy.s10p31s102be.profile.infra.entity.ProfileColumnDictionary;
import com.ssafy.s10p31s102be.profile.infra.enums.ProfileColumnDataType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileColumnDto {
    private String name;
    private String label;
    private Boolean required;
    private ProfileColumnDataType type;
    private List<String> options;

    public static ProfileColumnDto fromEntity(ProfileColumn profileColumn) {
        return ProfileColumnDto.builder()
                .name(profileColumn.getName())
                .label(profileColumn.getLabel())
                .required(profileColumn.getRequired())
                .type(profileColumn.getDataType())
                .options(profileColumn.getProfileColumnDictionaries().stream().map(ProfileColumnDictionary::getContent).toList())
                .build();
    }
}

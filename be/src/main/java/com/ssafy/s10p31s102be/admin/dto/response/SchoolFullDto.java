package com.ssafy.s10p31s102be.admin.dto.response;

import com.ssafy.s10p31s102be.profile.infra.entity.education.School;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolFullDto {
    private Integer id;
    private String schoolName;
    private String country;

    public static SchoolFullDto fromEntity(School s) {
        return SchoolFullDto.builder()
                .id(s.getId())
                .schoolName( s.getSchoolName() )
                .country( s.getCountry() )
                .build();
    }
}

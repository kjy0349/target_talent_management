package com.ssafy.s10p31s102be.profile.dto.response;

import com.ssafy.s10p31s102be.profile.infra.entity.education.School;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SchoolDetailDto {
    private Integer id;
    private String schoolName;
    private String country;

    public static SchoolDetailDto fromEntity(School school) {
        return SchoolDetailDto.builder()
                .id(school.getId())
                .schoolName(school.getSchoolName())
                .country(school.getCountry())
                .build();
    }
}

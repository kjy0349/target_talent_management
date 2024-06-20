package com.ssafy.s10p31s102be.techmap.dto.response;

import com.ssafy.s10p31s102be.profile.infra.entity.education.Lab;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapProjectLab;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TechLabReadDto {
    private Integer id;
    private String schoolName;
    private String major;
    private String professor;
    private String data;

    public static TechLabReadDto fromEntity(TechmapProjectLab techmapProjectLab) {
        return TechLabReadDto.builder()
                .id(techmapProjectLab.getLab().getLabId())
                .schoolName(techmapProjectLab.getLab().getSchool().getSchoolName())
                .major(techmapProjectLab.getLab().getMajor())
                .professor(techmapProjectLab.getLab().getLabProfessor())
                .data(techmapProjectLab.getLab().getLabName())
                .build();
    }

    public static TechLabReadDto fromEntity(Lab lab) {
        return TechLabReadDto.builder()
                .id(lab.getLabId())
                .schoolName(lab.getSchool().getSchoolName())
                .major(lab.getMajor())
                .professor(lab.getLabProfessor())
                .data(lab.getLabName())
                .build();
    }
}

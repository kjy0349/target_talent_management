package com.ssafy.s10p31s102be.profile.dto.response;

import com.ssafy.s10p31s102be.profile.dto.request.SpecializationCreateDto;
import com.ssafy.s10p31s102be.profile.infra.entity.career.Specialization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpecializationDetailDto {
    private Long id;
    private String specialPoint;
    private String description;
    private Boolean isFavorite;
    private String memberDepartment;
    private String memberName;
    private LocalDateTime createdAt;

    public static SpecializationDetailDto fromEntity(Specialization specialization) {
        return SpecializationDetailDto.builder()
                .id(specialization.getId())
                .specialPoint(specialization.getSpecialPoint())
                .description(specialization.getDescription())
                .isFavorite(specialization.getIsFavorite())
                .memberDepartment(specialization.getMemberDepartment())
                .memberName(specialization.getMemberName())
                .createdAt(specialization.getCreatedAt())
                .build();
    }
}

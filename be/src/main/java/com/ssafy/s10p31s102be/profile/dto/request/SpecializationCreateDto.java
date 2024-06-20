package com.ssafy.s10p31s102be.profile.dto.request;

import com.ssafy.s10p31s102be.profile.infra.entity.career.Specialization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpecializationCreateDto {
    private String specialPoint;
    private String description;
}

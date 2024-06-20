package com.ssafy.s10p31s102be.profile.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabCreateDto {
    private Integer schoolId;
    private String major;
    private String professor;
    private String labName;
}

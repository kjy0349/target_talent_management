package com.ssafy.s10p31s102be.profile.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TechLabDto {
    private Integer labId;
    private String schoolName;
    private String major;
    private String labProfessor;
    private String labName;
}

package com.ssafy.s10p31s102be.techmap.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TargetYearReadDto {
    private Integer targetYear;
    private Long count;
}

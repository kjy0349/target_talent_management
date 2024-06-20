package com.ssafy.s10p31s102be.networking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NetworkingSearchCondition {
    private Integer targetYear;
    private Integer departmentId;
    private String category;

}

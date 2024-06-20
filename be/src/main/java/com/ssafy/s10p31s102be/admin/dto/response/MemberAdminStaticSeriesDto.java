package com.ssafy.s10p31s102be.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MemberAdminStaticSeriesDto {
    private String name;
    private List<Double> data;
    private String color;
}

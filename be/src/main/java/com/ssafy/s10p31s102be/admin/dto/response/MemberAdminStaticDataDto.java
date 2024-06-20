package com.ssafy.s10p31s102be.admin.dto.response;

import com.ssafy.s10p31s102be.admin.dto.response.MemberAdminStaticDto;
import com.ssafy.s10p31s102be.admin.dto.response.MemberAdminStaticSeriesDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MemberAdminStaticDataDto {
    private int sales;
    private double percentage;
    private List<String> categories;
    private List<MemberAdminStaticSeriesDto> series;
}

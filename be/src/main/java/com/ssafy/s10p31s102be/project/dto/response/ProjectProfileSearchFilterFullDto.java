package com.ssafy.s10p31s102be.project.dto.response;

import com.ssafy.s10p31s102be.admin.dto.response.JobRankAdminSummaryDto;

import java.util.List;

//TODO 나중에 추가하기
public class ProjectProfileSearchFilterFullDto {
    List<String> degrees;
    List<String> employStatuses;
    List<JobRankAdminSummaryDto> jobRanks;
}

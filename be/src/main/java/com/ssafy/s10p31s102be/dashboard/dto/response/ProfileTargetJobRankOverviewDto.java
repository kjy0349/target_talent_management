package com.ssafy.s10p31s102be.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileTargetJobRankOverviewDto {
    List<String> labels;
    List<Integer> series;
    List<ProfileTargetJobRankTopChannel> topChannels;
}

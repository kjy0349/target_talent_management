package com.ssafy.s10p31s102be.profile.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileManagerUpdateDto {
    private List<Integer> profileIds;
    private Integer memberId;
}

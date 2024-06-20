package com.ssafy.s10p31s102be.networking.dto.request;

import com.ssafy.s10p31s102be.networking.infra.enums.NetworkingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NetworkingUpdateDto {
    private Integer networkingId;
    private Integer memberId;
    private String category;
    private String selectDescription;
    private Integer executiveId;
    private NetworkingStatus networkingStatus;
    private List<Integer> networkingProfileIds;
}

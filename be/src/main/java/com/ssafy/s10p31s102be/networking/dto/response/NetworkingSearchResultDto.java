package com.ssafy.s10p31s102be.networking.dto.response;

import com.ssafy.s10p31s102be.admin.dto.response.networking.NetworkingAdminSummaryDto;
import com.ssafy.s10p31s102be.networking.infra.entity.Networking;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NetworkingSearchResultDto {
    List<NetworkingSummaryDto> networkingSummaryDtos;
    Integer count;

    public static NetworkingSearchResultDto fromEntity(Page networkings){
        List<Networking> result = networkings.getContent();
        return NetworkingSearchResultDto.builder()
                .count(networkings.getTotalPages())
                .networkingSummaryDtos(result.stream()
                        .map(NetworkingSummaryDto::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}

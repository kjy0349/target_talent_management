package com.ssafy.s10p31s102be.networking.dto.response;


import com.ssafy.s10p31s102be.admin.dto.response.networking.ExecutiveAdminSummaryDto;
import com.ssafy.s10p31s102be.member.infra.entity.Executive;
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
public class ExecutiveSearchResultDto {
    List<ExecutiveSummaryDto> executiveAdminSummaryDtos;
    Integer count;

    public static ExecutiveSearchResultDto fromEntity(Page executives){
        List<Executive> result = executives.getContent();

        return ExecutiveSearchResultDto.builder()
                .count(executives.getTotalPages())
                .executiveAdminSummaryDtos(result.stream()
                        .map(ExecutiveSummaryDto::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}

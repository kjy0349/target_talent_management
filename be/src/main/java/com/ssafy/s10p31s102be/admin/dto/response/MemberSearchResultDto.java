package com.ssafy.s10p31s102be.admin.dto.response;

import com.ssafy.s10p31s102be.member.infra.entity.Member;
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
public class MemberSearchResultDto {
    List<MemberAdminFullDto> memberAdminFullDtos;
    Integer count;

    public static MemberSearchResultDto fromEntity(Page members){
        List<Member> result = members.getContent();
        return MemberSearchResultDto.builder()
                .count(members.getTotalPages())
                .memberAdminFullDtos(result.stream()
                        .map(MemberAdminFullDto::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}

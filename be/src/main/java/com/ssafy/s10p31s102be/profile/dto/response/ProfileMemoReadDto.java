package com.ssafy.s10p31s102be.profile.dto.response;

import com.ssafy.s10p31s102be.member.dto.response.MemberSummaryDto;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.profile.infra.entity.ProfileMemo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileMemoReadDto {
    private Long id;
    private Integer memberId;
    private String memberDepartment;
    private String memberName;
    private String content;
    private LocalDateTime createdAt;

    public static ProfileMemoReadDto fromEntity(ProfileMemo profileMemo) {
        return ProfileMemoReadDto.builder()
                .id(profileMemo.getId())
                .memberId(profileMemo.getMember().getId())
                .memberDepartment(profileMemo.getMember().getDepartment().getName())
                .memberName(profileMemo.getMember().getName())
                .content(profileMemo.getContent())
                .createdAt(profileMemo.getCreatedAt())
                .build();
    }
}

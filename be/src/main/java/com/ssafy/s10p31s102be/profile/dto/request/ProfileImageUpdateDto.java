package com.ssafy.s10p31s102be.profile.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileImageUpdateDto {
    @NotNull(message = "프로필 이미지 URL이 존재하지 않습니다.")
    private String url;
}
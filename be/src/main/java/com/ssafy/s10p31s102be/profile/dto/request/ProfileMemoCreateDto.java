package com.ssafy.s10p31s102be.profile.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileMemoCreateDto {
    @NotNull(message = "메모 내용이 존재하지 않습니다.")
    private String content;
    private Boolean isPrivate;
}
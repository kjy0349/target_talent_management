package com.ssafy.s10p31s102be.profile.dto.response;

import com.ssafy.s10p31s102be.common.infra.entity.Keyword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class KeywordPreviewDto {
    private String data;

    public static KeywordPreviewDto fromEntity(Keyword keyword) {
        return KeywordPreviewDto.builder()
                .data(keyword.getData())
                .build();
    }
}

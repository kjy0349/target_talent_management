package com.ssafy.s10p31s102be.common.dto.response;

import com.ssafy.s10p31s102be.common.infra.entity.Files;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilesSummaryDto {
    public Integer id;
    public String source;
    public String imgUrl;

    public static FilesSummaryDto fromEntity(Files files){
        FilesSummaryDto dto = FilesSummaryDto.builder()
                .id(files.getId())
                .source(files.getSource())
                .imgUrl( files.getImgUrl() )
                .build();
        return dto;
    }
}

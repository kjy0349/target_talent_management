package com.ssafy.s10p31s102be.techmap.dto.response;

import com.ssafy.s10p31s102be.techmap.infra.entity.TechMainCategory;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TechMainCategoryReadDto {
    private Integer mainCategoryId;
    private String mainCategory;

    public static TechMainCategoryReadDto fromEntity(TechMainCategory techMainCategory) {
        return TechMainCategoryReadDto.builder()
                .mainCategoryId(techMainCategory.getId())
                .mainCategory(techMainCategory.getTechMainCategoryName())
                .build();
    }
}

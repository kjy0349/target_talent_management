package com.ssafy.s10p31s102be.techmap.dto.response;

import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapProject;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TechmapPreviewDto {
    private Integer techmapId;
    private Integer targetYear;
    private String targetDepartmentName;
    private String techDetailName;
    private String techSubCategoryName;
    private String techMainCategoryName;

    public static TechmapPreviewDto fromEntity(TechmapProject techmapProject) {
        return TechmapPreviewDto.builder()
                .techmapId(techmapProject.getTechmap().getId())
                .targetYear(techmapProject.getTargetYear())
                .targetDepartmentName(techmapProject.getManager().getDepartment().getName())
                .techDetailName(techmapProject.getKeyword().getData())
                .techSubCategoryName(techmapProject.getTechSubCategory())
                .techMainCategoryName(techmapProject.getTechMainCategory().getTechMainCategoryName())
                .build();
    }
}

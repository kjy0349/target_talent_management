package com.ssafy.s10p31s102be.techmap.dto.response;

import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapProject;
import com.ssafy.s10p31s102be.techmap.infra.enums.TechCompanyRelativeLevel;
import com.ssafy.s10p31s102be.techmap.infra.enums.TechStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TechmapProjectReadDto {
    private Integer techmapProjectId;
    private String mainCategory;
    private String subCategory;
    private String techDetail;
    private TechStatus techStatus;
    private TechCompanyRelativeLevel techCompanyRelativeLevel;
    private String relativeLevelReason;
    private String description;
    private Boolean targetStatus;
    private Integer targetMemberCount;
    private Integer profileSize;
    private Integer techLabSize;
    private Integer techCompanySize;
    private String managerName;

    public static TechmapProjectReadDto fromEntity(TechmapProject techmapProject) {
        return TechmapProjectReadDto.builder()
                .techmapProjectId(techmapProject.getId())
                .mainCategory(techmapProject.getTechMainCategory().getTechMainCategoryName())
                .subCategory(techmapProject.getTechSubCategory())
                .techDetail(techmapProject.getKeyword().getData())
                .description(techmapProject.getDescription())
                .techStatus(techmapProject.getTechStatus())
                .techCompanyRelativeLevel(techmapProject.getTechCompanyRelativeLevel())
                .relativeLevelReason(techmapProject.getRelativeLevelReason())
                .targetStatus(techmapProject.getTargetStatus())
                .targetMemberCount(techmapProject.getTargetMemberCount())
                .profileSize(techmapProject.getTechmapProjectProfiles().size())
                .techLabSize(techmapProject.getTechmapProjectLabs().size())
                .techCompanySize(techmapProject.getTechmapProjectCompanies().size())
                .managerName(techmapProject.getManager().getName())
                .build();
    }

}

package com.ssafy.s10p31s102be.techmap.dto.request;

import com.ssafy.s10p31s102be.techmap.infra.enums.TechStatus;
import com.ssafy.s10p31s102be.techmap.infra.enums.TechCompanyRelativeLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TechmapProjectCreateDto {
    private Integer techmapId;
    private Integer mainCategoryId;
    private String subCategory;
    private String keyword;
    private TechStatus techStatus;
    private String description;
    private TechCompanyRelativeLevel relativeLevel;
    private String relativeLevelReason;
    private Boolean targetStatus;
    private Integer targetMemberCount;
}

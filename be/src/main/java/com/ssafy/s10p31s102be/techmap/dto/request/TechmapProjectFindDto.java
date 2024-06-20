package com.ssafy.s10p31s102be.techmap.dto.request;

import com.ssafy.s10p31s102be.techmap.infra.enums.TechCompanyRelativeLevel;
import com.ssafy.s10p31s102be.techmap.infra.enums.TechStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TechmapProjectFindDto {
    @NotNull
    private Integer pageNumber;
    @NotNull
    private Integer size;
    private Integer techmapId;
    private TechCompanyRelativeLevel techCompanyRelativeLevel;
    private Integer targetYear;
    private TechStatus techStatus;
    private Boolean targetStatus;
    private Integer departmentId;
}

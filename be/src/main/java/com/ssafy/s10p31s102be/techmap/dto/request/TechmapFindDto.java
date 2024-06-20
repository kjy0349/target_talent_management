package com.ssafy.s10p31s102be.techmap.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TechmapFindDto {
    @NotNull
    private Integer pageNumber;
    @NotNull
    private Integer size;
    private Integer targetYear;
    private Integer departmentId;
}

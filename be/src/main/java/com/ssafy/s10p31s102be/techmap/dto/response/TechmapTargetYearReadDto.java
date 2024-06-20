package com.ssafy.s10p31s102be.techmap.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechmapTargetYearReadDto {
    private Integer targetYear;
    private Long count;
    private List<Long> ids;
}

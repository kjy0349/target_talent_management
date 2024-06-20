package com.ssafy.s10p31s102be.techmap.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TechmapPageDto {
    private List<TechmapReadDto> techmaps;
    private List<TargetYearReadDto> targetYear;
    private Integer totalPages;
    private Long totalElements;
    private Integer currentPage;
    private Integer size;
}

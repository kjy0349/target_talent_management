package com.ssafy.s10p31s102be.techmap.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TechmapProjectPageDto {
    private List<TechmapProjectReadDto> techmapProjects;
    private List<TechmapTargetYearReadDto> targetYear;
    private Integer totalPages;
    private Long totalElements;
    private Integer currentPage;
    private Integer size;

}

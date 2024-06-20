package com.ssafy.s10p31s102be.board.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticlePageDto {
    private List<ArticleReadDto> articles;
    private Integer totalPages;
    private Long totalElements;
    private Integer currentPage;
    private Integer size;
}

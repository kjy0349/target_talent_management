package com.ssafy.s10p31s102be.board.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentPageDto {
    private List<CommentReadDto> comments;
    private Integer totalPages;
    private Long totalElements;
    private Integer currentPage;
    private Integer size;
}

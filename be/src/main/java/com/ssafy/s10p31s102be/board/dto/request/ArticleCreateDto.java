package com.ssafy.s10p31s102be.board.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleCreateDto {
    private Integer boardId;
    private String title;
    private String content;
    private String fileSource;
}

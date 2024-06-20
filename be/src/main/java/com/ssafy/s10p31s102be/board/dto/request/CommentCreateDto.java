package com.ssafy.s10p31s102be.board.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateDto {
    private Integer boardId;
    private Integer articleId;
    private String content;
}

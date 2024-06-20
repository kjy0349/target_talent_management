package com.ssafy.s10p31s102be.board.dto.response;

import com.ssafy.s10p31s102be.board.infra.entity.Comment;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentReadDto {
    private Integer id;
    private String content;
    private String writer;

    public static CommentReadDto fromEntity(Comment comment){
        return CommentReadDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .writer(comment.getMember().getName())
                .build();
    }
}

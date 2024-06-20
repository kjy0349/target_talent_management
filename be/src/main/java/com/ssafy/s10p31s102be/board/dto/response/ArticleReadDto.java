package com.ssafy.s10p31s102be.board.dto.response;

import com.ssafy.s10p31s102be.board.infra.entity.Article;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleReadDto {
    private Integer id;
    private String title;
    private String content;
    private String fileSource;
    private String writer;
    private Integer viewCount;
    private LocalDateTime createdAt;

    public static ArticleReadDto fromEntity(Article article){
        return ArticleReadDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .fileSource(article.getFileSource())
                .writer(article.getMember().getName())
                .viewCount(article.getViewCount())
                .createdAt(article.getCreatedAt())
                .build();
    }

    public static ArticleReadDto fromEntityNoWriter(Article article){
        return ArticleReadDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .fileSource(article.getFileSource())
                .viewCount(article.getViewCount())
                .createdAt(article.getCreatedAt())
                .build();
    }

    public static ArticleReadDto fromEntityNoViewCount(Article article){
        return ArticleReadDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .fileSource(article.getFileSource())
                .writer(article.getMember().getName())
                .createdAt(article.getCreatedAt())
                .build();
    }

    public static ArticleReadDto fromEntityNoViewAndWriter(Article article) {
        return ArticleReadDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .fileSource(article.getFileSource())
                .createdAt(article.getCreatedAt())
                .build();
    }
}

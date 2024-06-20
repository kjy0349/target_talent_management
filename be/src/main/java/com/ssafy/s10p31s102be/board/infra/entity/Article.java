package com.ssafy.s10p31s102be.board.infra.entity;

import com.ssafy.s10p31s102be.common.infra.entity.BaseTimeEntity;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Article extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Integer id;

    @Builder
    public Article(String title, String content, String fileSource, Integer viewCount,
                   Member member, Board board) {
        this.title = title;
        this.content = content;
        this.fileSource = fileSource;
        this.viewCount = viewCount;
        this.member = member;
        this.board = board;
    }

    private String title;

    private String content;

    private Integer viewCount;

    private String fileSource;

    @ManyToOne( fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn( name = "member_id" )
    private Member member;

    @ManyToOne( fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn( name = "board_id" )
    private Board board;

    @OneToMany( mappedBy = "article" )
    private List<Comment> comments = new ArrayList<>();

    public void updateArticle(String title, String content, String fileSource){
        this.title = title;
        this.content = content;
        this.fileSource = fileSource;
    }

    public void updateViewCount(){
        this.viewCount++;
    }
}

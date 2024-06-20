package com.ssafy.s10p31s102be.board.infra.entity;

import com.ssafy.s10p31s102be.common.infra.entity.BaseTimeEntity;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @Builder
    public Comment(String content, Member member, Article article) {
        this.content = content;
        this.member = member;
        this.article = article;
    }

    private String content;

    @ManyToOne( fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn( name = "member_id" )
    private Member member;

    @ManyToOne( fetch = FetchType.LAZY, cascade = CascadeType.PERSIST )
    @JoinColumn( name= "article_id" )
    private Article article;

    public void update(String content){
        this.content = content;
    }
}

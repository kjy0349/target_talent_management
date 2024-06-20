package com.ssafy.s10p31s102be.board.infra.entity;

import com.ssafy.s10p31s102be.admin.infra.entity.Menu;
import com.ssafy.s10p31s102be.common.infra.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Board extends BaseTimeEntity {
    //TODO 권한 관리
    @Id
    @GeneratedValue
    private Integer id;

    @Builder
    public Board(String name, Integer readAuthorityLevel, Integer writeAuthorityLevel, Integer manageAuthorityLevel, Boolean isCommentUsed, Boolean canViewCount, Boolean canViewWriter) {
        this.name = name;
        this.readAuthorityLevel = readAuthorityLevel;
        this.writeAuthorityLevel = writeAuthorityLevel;
        this.manageAuthorityLevel = manageAuthorityLevel;
        this.isCommentUsed = isCommentUsed;
        this.canViewCount = canViewCount;
        this.canViewWriter = canViewWriter;
    }

    private String name;

    private Integer readAuthorityLevel;

    private Integer writeAuthorityLevel;

    private Integer manageAuthorityLevel;

    private Boolean isCommentUsed;

    private Boolean canViewCount;

    private Boolean canViewWriter;

    @OneToMany(mappedBy = "board")
    private List<Article> articles = new ArrayList<>();

    @OneToMany(mappedBy = "board")
    private List<BoardDepartment> boardDepartments = new ArrayList<>();

    @OneToOne(mappedBy = "board")
    private Menu menu;
}

package com.ssafy.s10p31s102be.admin.infra.entity;

import com.ssafy.s10p31s102be.board.infra.entity.Board;
import com.ssafy.s10p31s102be.common.infra.enums.TextOption;
import com.ssafy.s10p31s102be.common.infra.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Menu extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private Boolean isDeleted;

    private Boolean isBoard;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="textEffect", column=@Column(name="before_hover_text_effect")),
            @AttributeOverride(name="textSort", column=@Column(name="before_hover_text_sort")),
            @AttributeOverride(name="textColor", column=@Column(name="before_hover_text_color"))
    })
    private TextOption beforeHoverTextOption;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="textEffect", column=@Column(name="after_hover_text_effect")),
            @AttributeOverride(name="textSort", column=@Column(name="after_hover_text_sort")),
            @AttributeOverride(name="textColor", column=@Column(name="after_hover_text_color"))
    })
    private TextOption afterHoverTextOption;

    @OneToOne( fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Board board;
}

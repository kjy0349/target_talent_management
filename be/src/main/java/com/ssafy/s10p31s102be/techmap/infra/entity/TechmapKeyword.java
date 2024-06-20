package com.ssafy.s10p31s102be.techmap.infra.entity;


import com.ssafy.s10p31s102be.common.infra.entity.BaseTimeEntity;
import com.ssafy.s10p31s102be.common.infra.entity.Keyword;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class TechmapKeyword extends BaseTimeEntity {
    public TechmapKeyword(Techmap techmap, Keyword keyword) {
        this.techmap = techmap;
        this.keyword = keyword;
    }

    /**
     * TODO 이렇게만 봐서는 잘 모르겠는데 일단 erd 기준으로 작성 완료하고 추후 논의하자 - 단톡 병헌
     */


    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "techmap_id")
    private Techmap techmap;

    @ManyToOne( fetch = FetchType.LAZY, cascade = CascadeType.PERSIST )
    @JoinColumn(name = "keyword_id")
    private Keyword keyword;
}

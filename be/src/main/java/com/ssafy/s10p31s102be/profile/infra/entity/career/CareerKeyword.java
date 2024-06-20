package com.ssafy.s10p31s102be.profile.infra.entity.career;

import com.ssafy.s10p31s102be.common.infra.entity.Keyword;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class CareerKeyword {

    @Builder
    public CareerKeyword(Career career, Keyword keyword) {
        this.career = career;
        this.keyword = keyword;
    }

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "career_id", nullable = false)
    private Career career;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "keyword_id", nullable = false)
    private Keyword keyword;
}

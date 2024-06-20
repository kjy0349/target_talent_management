package com.ssafy.s10p31s102be.profile.infra.entity.education;

import com.ssafy.s10p31s102be.common.infra.entity.Keyword;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class KeywordEducation {

    @Builder
    public KeywordEducation(Education education, Keyword keyword) {
        this.education = education;
        this.keyword = keyword;
    }

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "education_id", nullable = false)
    private Education education;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "keyword_id", nullable = false)
    private Keyword keyword;
}

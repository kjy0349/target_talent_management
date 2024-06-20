package com.ssafy.s10p31s102be.profile.infra.entity;

import com.ssafy.s10p31s102be.common.infra.entity.BaseTimeEntity;
import com.ssafy.s10p31s102be.common.infra.entity.Keyword;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ProfileKeyword extends BaseTimeEntity {
    @Id
    @GeneratedValue
    private Long id;

    public ProfileKeyword(Profile profile, Keyword keyword) {
        this.profile = profile;
        this.keyword = keyword;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "keyword_id", nullable = false)
    private Keyword keyword;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;
}

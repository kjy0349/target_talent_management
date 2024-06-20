package com.ssafy.s10p31s102be.profile.infra.entity;

import com.ssafy.s10p31s102be.common.infra.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ProfileColumnDictionary extends BaseTimeEntity {

    @Builder
    public ProfileColumnDictionary(String content, ProfileColumn profileColumn) {
        this.content = content;
        this.profileColumn = profileColumn;
    }

    @Id
    @GeneratedValue
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_column_key", nullable = false)
    private ProfileColumn profileColumn;
}

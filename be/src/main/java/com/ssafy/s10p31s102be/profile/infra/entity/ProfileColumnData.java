package com.ssafy.s10p31s102be.profile.infra.entity;

import com.ssafy.s10p31s102be.common.infra.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ProfileColumnData extends BaseTimeEntity {

    @Builder
    public ProfileColumnData(String content, ProfileColumn profileColumn, Profile profile) {
        this.content = content;
        this.profileColumn = profileColumn;
        this.profile = profile;
    }

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_column_key", nullable = false)
    private ProfileColumn profileColumn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    public void updateContent(String content) { this.content = content; }
}

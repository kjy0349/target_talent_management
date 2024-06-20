package com.ssafy.s10p31s102be.profile.infra.entity;

import com.ssafy.s10p31s102be.common.infra.entity.BaseTimeEntity;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ProfileMemo extends BaseTimeEntity {

    @Builder
    public ProfileMemo(String content, Boolean isPrivate, Member member, Profile profile) {
        this.content = content;
        this.isPrivate = isPrivate;

        this.memberName = member.getName();
        this.memberDepartment = member.getDepartment().getName();
        this.memberProfileImage = member.getProfileImage();

        this.member = member;
        this.profile = profile;
    }

    @Id
    @GeneratedValue
    private Long id;

    @Lob
    @Size(max = 1000)
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Boolean isPrivate;

    private String memberName;

    private String memberDepartment;

    private String memberProfileImage;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    public void update(Member member, String content, Boolean isPrivate) {
        this.memberName = member.getName();
        this.memberDepartment = member.getDepartment().getName();
        this.memberProfileImage = member.getProfileImage();
        this.content = content;
        this.isPrivate = isPrivate;
    }
}

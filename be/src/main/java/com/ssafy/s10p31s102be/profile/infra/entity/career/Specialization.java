package com.ssafy.s10p31s102be.profile.infra.entity.career;

import com.ssafy.s10p31s102be.common.infra.entity.BaseTimeEntity;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.profile.dto.request.SpecializationCreateDto;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Specialization extends BaseTimeEntity {
    @Builder
    public Specialization(String specialPoint, String description, Member member, Profile profile) {
        this.specialPoint = specialPoint;
        this.description = description;
        this.memberDepartment = member.getDepartment().getName();
        this.memberName = member.getName();
        this.profile = profile;

        this.isFavorite = Boolean.FALSE;
        this.modifiedAt = LocalDateTime.now();
    }

    @Id
    @GeneratedValue
    private Long id;

    private String specialPoint;

    @Lob
    @Size(max = 1000)
    private String description;

    @Column(nullable = false)
    private Boolean isFavorite;

    @Column(nullable = false)
    private String memberDepartment;

    @Column(nullable = false)
    private String memberName;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    public void update(String specialPoint, String description, Member member) {
        this.specialPoint = specialPoint;
        this.description = description;
        this.memberDepartment = member.getDepartment().getName();
        this.memberName = member.getName();

        this.modifiedAt = LocalDateTime.now();
    }

    public void updateFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
}

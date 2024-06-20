package com.ssafy.s10p31s102be.techmap.infra.entity;

import com.ssafy.s10p31s102be.common.infra.entity.BaseTimeEntity;
import com.ssafy.s10p31s102be.common.infra.entity.Keyword;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.techmap.infra.enums.TechCompanyRelativeLevel;
import com.ssafy.s10p31s102be.techmap.infra.enums.TechStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class TechmapProject extends BaseTimeEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @Builder
    public TechmapProject(String techSubCategory, TechStatus techStatus, String description, TechCompanyRelativeLevel techCompanyRelativeLevel, String relativeLevelReason,
                          Boolean targetStatus, Integer targetMemberCount, Integer departmentId, Integer targetYear,
                          Member manager, Techmap techmap, TechMainCategory techMainCategory, Keyword keyword) {
        this.techSubCategory = techSubCategory;
        this.techStatus = techStatus;
        this.description = description;
        this.techCompanyRelativeLevel = techCompanyRelativeLevel;
        this.relativeLevelReason = relativeLevelReason;
        this.targetStatus = targetStatus;
        this.targetMemberCount = targetMemberCount;
        this.departmentId = departmentId;
        this.manager = manager;
        this.techmap = techmap;
        this.techMainCategory = techMainCategory;
        this.keyword = keyword;
        this.targetYear = targetYear;
    }

    private String techSubCategory;

    private TechStatus techStatus;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private TechCompanyRelativeLevel techCompanyRelativeLevel;

    private String relativeLevelReason;

    private Boolean targetStatus;

    private Integer targetMemberCount;

    private Integer departmentId;

    private Integer targetYear;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "techmap_id")
    private Techmap techmap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tech_main_category_id")
    private TechMainCategory techMainCategory;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "keyword_id")
    private Keyword keyword;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member manager;

    @OneToMany(mappedBy = "techmapProject", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TechmapProjectMember> techmapProjectMembers = new ArrayList<>();

    @OneToMany(mappedBy = "techmapProject", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TechmapProjectProfile> techmapProjectProfiles = new ArrayList<>();

    @OneToMany(mappedBy = "techmapProject", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TechmapProjectLab> TechmapProjectLabs = new ArrayList<>();

    @OneToMany(mappedBy = "techmapProject", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TechmapProjectCompany> techmapProjectCompanies = new ArrayList<>();

    public void updatetechmapProject(String techSubCategory, TechStatus techStatus, String description, TechCompanyRelativeLevel techCompanyRelativeLevel, String relativeLevelReason,
                                     Boolean targetStatus, Integer targetMemberCount,
                                     TechMainCategory techMainCategory, Keyword keyword){
        this.techSubCategory = techSubCategory;
        this.techStatus = techStatus;
        this.description = description;
        this.techCompanyRelativeLevel = techCompanyRelativeLevel;
        this.relativeLevelReason = relativeLevelReason;
        this.targetStatus = targetStatus;
        this.targetMemberCount = targetMemberCount;
        this.techMainCategory = techMainCategory;
        this.keyword = keyword;
    }

    public void updateManager(Member manager){
        this.manager = manager;
    }
}

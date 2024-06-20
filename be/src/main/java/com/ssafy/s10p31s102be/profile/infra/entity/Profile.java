package com.ssafy.s10p31s102be.profile.infra.entity;

import com.ssafy.s10p31s102be.common.infra.entity.BaseTimeEntity;
import com.ssafy.s10p31s102be.common.infra.entity.Keyword;
import com.ssafy.s10p31s102be.member.infra.entity.JobRank;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.networking.infra.entity.NetworkingProfile;
import com.ssafy.s10p31s102be.profile.infra.entity.career.Career;
import com.ssafy.s10p31s102be.profile.infra.entity.career.Specialization;
import com.ssafy.s10p31s102be.profile.infra.entity.community.*;
import com.ssafy.s10p31s102be.profile.infra.entity.education.Education;
import com.ssafy.s10p31s102be.project.infra.entity.ProjectProfile;
import com.ssafy.s10p31s102be.profile.infra.enums.EmployStatus;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapProjectProfile;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Profile extends BaseTimeEntity {

    /*
        Profile Entity
        엔티티의 복잡도를 고려하여 컬럼 별 nullable 여부를 명시적으로 선언했습니다.

        필수 동적 컬럼 (required: true)
        - name (원본이름) string
        - foundNation (발굴 국가) string
        - foundPath (발굴 경로) string
        - column1 (column1) number
        - language (언어) string
        - column1 (column1) string
        - country (거주지) string
        - relocation (이주 의지) enum("상", "중", "하")

        추가 동적 컬럼 (required: false)
        - nameEng (영문이름) string
        - foundLocation (발굴 지역) string
        - phone (전화번호) string
        - email (이메일) string
        - visa (비자) enum("시민권", "영주권", "비대상")
        - referenceFile (참고 파일 URL) string
        - recommenderInfo (추천인 정보) string
        - recommenderRelation (추천인 관계) string
        - isF (외국인 여부) boolean
        - isS (isS 여부) boolean
        - specialDescription (isS 사유) string
        - url (link URL) string
        - homepageUrl (개인 홈페이지 URL) string

        동적 컬럼이 아닌, 연관관계로 엔티티에 종속된 데이터
    */

    @Builder
    public Profile(String founder, Member manager, String profileImage, JobRank targetJobRank, Boolean isPrivate, Boolean isAllCompany) {
        this.manager = manager;
        this.founder = founder;
        this.profileImage = profileImage;
        this.targetJobRank = targetJobRank;
        this.isPrivate = isPrivate;
        this.isAllCompany = isAllCompany;

        this.modifiedAt = LocalDateTime.now();
    }

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = true)
    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployStatus employStatus = EmployStatus.FOUND;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean isPrivate = false;

    @Column(nullable = false)
    @ColumnDefault("true")
    private Boolean isAllCompany = true;

    @Column(nullable = true)
    private LocalDateTime statusModifiedAt = LocalDateTime.now();

    // 동적 컬럼 리스트
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfileColumnData> profileColumnDatas = new ArrayList<>();

    // 포함된 프로젝트 리스트
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectProfile> projectProfiles = new ArrayList<>();

    // 포함된 인재Pool 프로젝트 리스트
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TechmapProjectProfile> techmapProjectProfiles = new ArrayList<>();

    // 학력 리스트
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Education> educations = new ArrayList<>();

    // 경력 리스트
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Career> careers = new ArrayList<>();

    // special 리스트
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Specialization> specializations = new ArrayList<>();

    // 면담 리스트
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Meeting> meetings = new ArrayList<>();

    // 활용계획 리스트
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsagePlan> usagePlans = new ArrayList<>();

    // 면접 리스트
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Interview> interviews = new ArrayList<>();

    // 채용전형 리스트
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmploymentHistory> employmentHistories = new ArrayList<>();

    // 키워드 리스트
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfileKeyword> profileKeywords = new ArrayList<>();

    // 메모 리스트
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfileMemo> profileMemos = new ArrayList<>();

    // 세부 기술
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id", nullable = true)
    private Keyword keyword;

    // 발굴자
    private String founder;

    // 담당자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = true)
    private Member manager;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NetworkingProfile> networkings = new ArrayList<>();

    // 타겟 경력개발단계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_rank_id", nullable = true)
    private JobRank targetJobRank;

    public void updateJobRank(JobRank jobRank) {
        this.targetJobRank = jobRank;

        this.modifiedAt = LocalDateTime.now();
    }

    public void updateManager(Member member) {
        this.manager = member;
    }

    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void updateStatus(EmployStatus employStatus) {
        this.employStatus = employStatus;
        this.statusModifiedAt = LocalDateTime.now();
    }
//    public void updateNetworking( Networking networking ){
//        this.networking = networking;
//    }

    public void updateOpenStatus(Boolean isPrivate, Boolean isAllCompany) {
        this.isPrivate = isPrivate;
        this.isAllCompany = isAllCompany;
    }
}
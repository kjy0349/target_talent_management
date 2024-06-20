package com.ssafy.s10p31s102be.common.service;

import com.ssafy.s10p31s102be.member.infra.entity.*;
import com.ssafy.s10p31s102be.member.infra.repository.*;
import com.ssafy.s10p31s102be.networking.infra.repository.ExecutiveJpaRepository;
import com.ssafy.s10p31s102be.profile.infra.entity.Country;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.entity.ProfileColumn;
import com.ssafy.s10p31s102be.profile.infra.entity.ProfileColumnDictionary;
import com.ssafy.s10p31s102be.profile.infra.entity.career.Company;
import com.ssafy.s10p31s102be.profile.infra.entity.education.Lab;
import com.ssafy.s10p31s102be.profile.infra.entity.education.School;
import com.ssafy.s10p31s102be.profile.infra.enums.ProfileColumnDataType;
import com.ssafy.s10p31s102be.profile.infra.repository.*;
import com.ssafy.s10p31s102be.project.dto.request.ProjectCreateDto;
import com.ssafy.s10p31s102be.project.infra.entity.*;
import com.ssafy.s10p31s102be.project.infra.repository.ProjectJpaRepository;
import com.ssafy.s10p31s102be.project.service.ProjectServiceImpl;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechMainCategory;
import com.ssafy.s10p31s102be.techmap.infra.repository.TechMainCategoryJpaRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InitService {
    private final PasswordEncoder passwordEncoder;
    private final MemberJpaRepository memberJpaRepository;
    private final DepartmentJpaRepository departmentJpaRepository;
    private final ProjectJpaRepository projectJpaRepository;
    private final ProjectServiceImpl projectService;
    private final JobRankJpaRepository jobRankJpaRepository;
    private final AuthorityJpaRepository authorityJpaRepository;
    private final ProfileJpaRepository profileJpaRepository;
    private final TeamJpaRepository teamJpaRepository;
    private final TechMainCategoryJpaRepository techMainCategoryJpaRepository;
    private final ExecutiveJpaRepository executiveJpaRepository;
    private final CountryJpaRepository countryJpaRepository;
    private final CompanyJpaRepository companyJpaRepository;
    private final ProfileColumnJpaRepository profileColumnJpaRepository;
    private final SchoolJpaRepository schoolJpaRepository;
    private final LabJpaRepository labJpaRepository;
    private final ProfileColumnDictionaryJpaRepository profileColumnDictionaryJpaRepository;

    //@PostConstruct
    public void save(){

        // Authority
        Authority a1 = Authority.builder()
                .authName("관리자")
                .authLevel(1)
                .build();

        Authority a2 = Authority.builder()
                .authName("운영진")
                .authLevel(2)
                .build();

        Authority a3 = Authority.builder()
                .authName("채용부서장")
                .authLevel(3)
                .build();

        Authority a4 = Authority.builder()
                .authName("채용담당자")
                .authLevel(4)
                .build();

        Authority a5 = Authority.builder()
                .authName("어시스턴트")
                .authLevel(5)
                .build();

        authorityJpaRepository.saveAll(Arrays.asList(a1, a2, a3, a4, a5));

        // JobRank

        JobRank j1 = JobRank.builder()
                .description("직급1")
                .build();

        JobRank j2 = JobRank.builder()
                .description("C2")
                .build();

        JobRank j3 = JobRank.builder()
                .description("직급3")
                .build();

        JobRank j4 = JobRank.builder()
                .description("직급4")
                .build();

        JobRank j5 = JobRank.builder()
                .description("직급4")
                .build();
        JobRank j6 = JobRank.builder()
                .description("부직급1")
                .build();
        JobRank j7 = JobRank.builder()
                .description("직급1")
                .build();

        jobRankJpaRepository.saveAll(Arrays.asList(j1, j2, j3, j4, j5, j6, j7));

        // Country

        Country ko = Country.builder()
                .name("한국")
                .code("KR")
                .continent("Asia")
                .build();

        Country us = Country.builder()
                .name("미국")
                .code("US")
                .continent("North America")
                .build();

        Country ca = Country.builder()
                .name("캐나다")
                .code("CA")
                .continent("North America")
                .build();

        Country gb = Country.builder()
                .name("영국")
                .code("GB")
                .continent("Europe")
                .build();

        Country ch = Country.builder()
                .name("스위스")
                .code("CH")
                .continent("Europe")
                .build();

        Country fr = Country.builder()
                .name("프랑스")
                .code("FR")
                .continent("Europe")
                .build();

        Country de = Country.builder()
                .name("독일")
                .code("DE")
                .continent("Europe")
                .build();

        Country se = Country.builder()
                .name("스웨덴")
                .code("SE")
                .continent("Europe")
                .build();

        Country jp = Country.builder()
                .name("일본")
                .code("JP")
                .continent("Asia")
                .build();

        Country sg = Country.builder()
                .name("싱가폴")
                .code("SG")
                .continent("Asia")
                .build();

        Country cn = Country.builder()
                .name("중국")
                .code("CN")
                .continent("Asia")
                .build();

        Country hk = Country.builder()
                .name("중국(홍콩)")
                .code("HK")
                .continent("Asia")
                .build();

        Country tw = Country.builder()
                .name("대만")
                .code("TW")
                .continent("Asia")
                .build();

        Country in = Country.builder()
                .name("인도")
                .code("IN")
                .continent("Asia")
                .build();

        countryJpaRepository.saveAll(Arrays.asList(ko, us, ca, gb, ch, fr, de, se, jp, sg, cn, hk, tw, in));

        // Department

        Department d1 = Department.builder()
                .name("VD")
                .description("VD")
                .build();

        Department d2 = Department.builder()
                .name("생활가전")
                .description("생활가전")
                .build();

        Department d3 = Department.builder()
                .name("MX")
                .description("MX")
                .build();

        Department d4 = Department.builder()
                .name("네트워크")
                .description("네트워크")
                .build();

        Department d5 = Department.builder()
                .name("SR")
                .description("삼성리서치")
                .build();

        Department d6 = Department.builder()
                .name("디자인")
                .description("디자인경영센터")
                .build();

        Department d7 = Department.builder()
                .name("한국총괄")
                .description("한국총괄")
                .build();

        Department d8 = Department.builder()
                .name("생산기술연구소")
                .description("생기硏")
                .build();

        Department d9 = Department.builder()
                .name("경영지원실")
                .description("경영지원실")
                .build();

        Department d10 = Department.builder()
                .name("전사기타")
                .description("전사기타")
                .build();

        departmentJpaRepository.saveAll(Arrays.asList(d1, d2, d3, d4, d5, d6, d7, d8, d9, d10));

        // Team

        Team t1 = Team.builder()
                .name("Talent Management그룹")
                .description("Talent Management그룹")
                .build();

        Team t2 = Team.builder()
                .name("Talent Acquisition그룹")
                .description("Talent Acquisition그룹")
                .build();

        Team t3 = Team.builder()
                .name("Employee Engagement그룹")
                .description("Employee Engagement그룹")
                .build();

        Team t4 = Team.builder()
                .name("People팀")
                .description("People팀")
                .build();

        teamJpaRepository.saveAll(Arrays.asList(t1, t2, t3, t4));

        // TechMainCategory

        TechMainCategory tmc1 = new TechMainCategory("H/W");
        TechMainCategory tmc2 = new TechMainCategory("S/W");

        techMainCategoryJpaRepository.saveAll(Arrays.asList(tmc1, tmc2));

        // Member

        Member m1 = Member.builder()
                .name("관리자")
                .department(d1)
                .team(t1)
                .authority(a1)
                .knoxId("admin@samsung.com")
                .password(passwordEncoder.encode("1q2w3e4r!"))
                .build();

        memberJpaRepository.saveAll(Arrays.asList(m1));

        // Executive

        Executive e1 = Executive.builder()
                .name("김OO")
                .email("executive01@samsung.com")
                .jobRank("직급4")
                .department("DX")
                .build();

        Executive e2 = Executive.builder()
                .name("이OO")
                .email("executive02@samsung.com")
                .jobRank("직급4")
                .department("DS")
                .build();

        executiveJpaRepository.saveAll(Arrays.asList(e1, e2));

        // Company

        Company c1 = Company.builder()
                .name("삼성전자")
                .build();

        Company c2 = Company.builder()
                .name("멀티캠퍼스")
                .build();

        companyJpaRepository.saveAll(Arrays.asList(c1, c2));

        // School

        School sc1 = School.builder()
                .schoolName("서울대학교")
                .country("한국")
                .build();

        School sc2 = School.builder()
                .schoolName("인하대학교")
                .country("한국")
                .build();

        School sc3 = School.builder()
                .schoolName("인천대학교")
                .country("한국")
                .build();

        schoolJpaRepository.saveAll(Arrays.asList(sc3));

        // Lab

        Lab l1 = Lab.builder()
                .school(sc1)
                .labName("서울대학교 연구실")
                .labProfessor("김OO")
                .researchType("H/W")
                .researchDescription("하드웨어 연구")
                .researchResult("성공")
                .build();

        Lab l2 = Lab.builder()
                .school(sc2)
                .labName("인하대학교 연구실")
                .labProfessor("이OO")
                .researchType("S/W")
                .researchDescription("소프트웨어 연구")
                .researchResult("성공")
                .build();

        sc1.getLabs().add(l1);
        sc2.getLabs().add(l2);

        labJpaRepository.saveAll(Arrays.asList(l1, l2));

        saveProfileColumns();
    }

    private void saveProfileColumns() {
        // ProfileColumn

        List<ProfileColumn> profileColumns = new ArrayList<>();

        ProfileColumn name = ProfileColumn.builder()
                .name("name")
                .label("한글이름")
                .dataType(ProfileColumnDataType.STRING)
                .required(true)
                .isPreview(true)
                .build();
        profileColumns.add(name);

        ProfileColumn nameEng = ProfileColumn.builder()
                .name("nameEng")
                .label("영어이름")
                .dataType(ProfileColumnDataType.STRING)
                .required(false)
                .isPreview(true)
                .build();
        profileColumns.add(nameEng);

        ProfileColumn column1 = ProfileColumn.builder()
                .name("column1")
                .label("column1")
                .dataType(ProfileColumnDataType.NUMBER)
                .required(false)
                .isPreview(true)
                .build();
        profileColumns.add(column1);

        ProfileColumn foundCountry = ProfileColumn.builder()
                .name("foundCountry")
                .label("발굴국가")
                .dataType(ProfileColumnDataType.STRING)
                .required(false)
                .isPreview(true)
                .build();
        profileColumns.add(foundCountry);

        ProfileColumn foundRegion = ProfileColumn.builder()
                .name("foundRegion")
                .label("발굴지역")
                .dataType(ProfileColumnDataType.STRING)
                .required(false)
                .isPreview(true)
                .build();
        profileColumns.add(foundRegion);

        ProfileColumn column1 = ProfileColumn.builder()
                .name("column1")
                .label("column1")
                .dataType(ProfileColumnDataType.STRING)
                .required(false)
                .isPreview(true)
                .build();
        profileColumns.add(column1);

        ProfileColumn visa = ProfileColumn.builder()
                .name("visa")
                .label("비자유형")
                .dataType(ProfileColumnDataType.STRING)
                .required(false)
                .isPreview(true)
                .build();
        profileColumns.add(visa);

        ProfileColumn korean = ProfileColumn.builder()
                .name("korean")
                .label("한국어")
                .dataType(ProfileColumnDataType.STRING)
                .required(false)
                .isPreview(true)
                .build();
        profileColumns.add(korean);

        ProfileColumn relocation = ProfileColumn.builder()
                .name("relocation")
                .label("이주의지")
                .dataType(ProfileColumnDataType.STRING)
                .required(false)
                .isPreview(true)
                .build();
        profileColumns.add(relocation);

        ProfileColumn foundPath = ProfileColumn.builder()
                .name("foundPath")
                .label("발굴경로")
                .dataType(ProfileColumnDataType.STRING)
                .required(true)
                .isPreview(true)
                .build();
        profileColumns.add(foundPath);

        ProfileColumn recommenderInfo = ProfileColumn.builder()
                .name("recommenderInfo")
                .label("추천인 정보")
                .dataType(ProfileColumnDataType.STRING)
                .required(false)
                .isPreview(true)
                .build();
        profileColumns.add(recommenderInfo);

        ProfileColumn recommenderRelation = ProfileColumn.builder()
                .name("recommenderRelation")
                .label("추천인 관계")
                .dataType(ProfileColumnDataType.STRING)
                .required(false)
                .isPreview(true)
                .build();
        profileColumns.add(recommenderRelation);

        ProfileColumn skillMainCategory = ProfileColumn.builder()
                .name("skillMainCategory")
                .label("tech")
                .dataType(ProfileColumnDataType.STRING)
                .required(true)
                .isPreview(true)
                .build();
        profileColumns.add(skillMainCategory);

        ProfileColumn skillSubCategory = ProfileColumn.builder()
                .name("skillSubCategory")
                .label("기술분야")
                .dataType(ProfileColumnDataType.STRING)
                .required(false)
                .isPreview(true)
                .build();
        profileColumns.add(skillSubCategory);

        ProfileColumn skillDetail = ProfileColumn.builder()
                .name("skillDetail")
                .label("상세분야")
                .dataType(ProfileColumnDataType.STRING)
                .required(false)
                .isPreview(true)
                .build();
        profileColumns.add(skillDetail);

        ProfileColumn isF = ProfileColumn.builder()
                .name("isF")
                .label("isF")
                .dataType(ProfileColumnDataType.BOOLEAN)
                .required(false)
                .isPreview(true)
                .build();
        profileColumns.add(isF);

        ProfileColumn isS = ProfileColumn.builder()
                .name("isS")
                .label("isS")
                .dataType(ProfileColumnDataType.BOOLEAN)
                .required(false)
                .isPreview(true)
                .build();
        profileColumns.add(isS);

        ProfileColumn specialDescription = ProfileColumn.builder()
                .name("specialDescription")
                .label("isS 사유")
                .dataType(ProfileColumnDataType.STRING)
                .required(false)
                .isPreview(true)
                .build();
        profileColumns.add(specialDescription);

        ProfileColumn phone = ProfileColumn.builder()
                .name("phone")
                .label("전화번호")
                .dataType(ProfileColumnDataType.STRING)
                .required(false)
                .isPreview(true)
                .build();
        profileColumns.add(phone);

        ProfileColumn email = ProfileColumn.builder()
                .name("email")
                .label("이메일")
                .dataType(ProfileColumnDataType.STRING)
                .required(false)
                .isPreview(true)
                .build();
        profileColumns.add(email);

        ProfileColumn url = ProfileColumn.builder()
                .name("url")
                .label("link URL")
                .dataType(ProfileColumnDataType.STRING)
                .required(false)
                .isPreview(true)
                .build();
        profileColumns.add(url);

        ProfileColumn homepageUrl = ProfileColumn.builder()
                .name("homepageUrl")
                .label("개인홈페이지 URL")
                .dataType(ProfileColumnDataType.STRING)
                .required(false)
                .isPreview(true)
                .build();
        profileColumns.add(homepageUrl);

        profileColumnJpaRepository.saveAll(profileColumns);

        // "visa" column
        ProfileColumnDictionary pcd_visa_1 = ProfileColumnDictionary.builder()
                .profileColumn(visa)
                .content("시민권")
                .build();

        ProfileColumnDictionary pcd_visa_2 = ProfileColumnDictionary.builder()
                .profileColumn(visa)
                .content("영주권")
                .build();

        ProfileColumnDictionary pcd_visa_3 = ProfileColumnDictionary.builder()
                .profileColumn(visa)
                .content("비대상")
                .build();

// "korean" column
        ProfileColumnDictionary pcd_korean_1 = ProfileColumnDictionary.builder()
                .profileColumn(korean)
                .content("상")
                .build();

        ProfileColumnDictionary pcd_korean_2 = ProfileColumnDictionary.builder()
                .profileColumn(korean)
                .content("중")
                .build();

        ProfileColumnDictionary pcd_korean_3 = ProfileColumnDictionary.builder()
                .profileColumn(korean)
                .content("하")
                .build();

        ProfileColumnDictionary pcd_korean_4 = ProfileColumnDictionary.builder()
                .profileColumn(korean)
                .content("확인필요")
                .build();

// "relocation" column
        ProfileColumnDictionary pcd_relocation_1 = ProfileColumnDictionary.builder()
                .profileColumn(relocation)
                .content("상")
                .build();

        ProfileColumnDictionary pcd_relocation_2 = ProfileColumnDictionary.builder()
                .profileColumn(relocation)
                .content("중")
                .build();

        ProfileColumnDictionary pcd_relocation_3 = ProfileColumnDictionary.builder()
                .profileColumn(relocation)
                .content("하")
                .build();

        ProfileColumnDictionary pcd_relocation_4 = ProfileColumnDictionary.builder()
                .profileColumn(relocation)
                .content("확인필요")
                .build();

// "foundPath" column
        ProfileColumnDictionary pcd_foundPath_1 = ProfileColumnDictionary.builder()
                .profileColumn(foundPath)
                .content("link")
                .build();

        ProfileColumnDictionary pcd_foundPath_2 = ProfileColumnDictionary.builder()
                .profileColumn(foundPath)
                .content("채용담당자")
                .build();

        ProfileColumnDictionary pcd_foundPath_3 = ProfileColumnDictionary.builder()
                .profileColumn(foundPath)
                .content("캠퍼스 리쿠르팅")
                .build();

        ProfileColumnDictionary pcd_foundPath_4 = ProfileColumnDictionary.builder()
                .profileColumn(foundPath)
                .content("네트워킹 행사")
                .build();

        ProfileColumnDictionary pcd_foundPath_5 = ProfileColumnDictionary.builder()
                .profileColumn(foundPath)
                .content("삼성채용홈페이지")
                .build();

        ProfileColumnDictionary pcd_foundPath_6 = ProfileColumnDictionary.builder()
                .profileColumn(foundPath)
                .content("사내추천")
                .build();

        ProfileColumnDictionary pcd_foundPath_7 = ProfileColumnDictionary.builder()
                .profileColumn(foundPath)
                .content("서치펌")
                .build();

        ProfileColumnDictionary pcd_foundPath_8 = ProfileColumnDictionary.builder()
                .profileColumn(foundPath)
                .content("자체발굴")
                .build();

        ProfileColumnDictionary pcd_foundPath_9 = ProfileColumnDictionary.builder()
                .profileColumn(foundPath)
                .content("기타")
                .build();

// "skillMainCategory" column
        ProfileColumnDictionary pcd_skillMainCategory_1 = ProfileColumnDictionary.builder()
                .profileColumn(skillMainCategory)
                .content("연구개발")
                .build();

        ProfileColumnDictionary pcd_skillMainCategory_2 = ProfileColumnDictionary.builder()
                .profileColumn(skillMainCategory)
                .content("SW개발")
                .build();

        ProfileColumnDictionary pcd_skillMainCategory_3 = ProfileColumnDictionary.builder()
                .profileColumn(skillMainCategory)
                .content("디자인")
                .build();

        ProfileColumnDictionary pcd_skillMainCategory_4 = ProfileColumnDictionary.builder()
                .profileColumn(skillMainCategory)
                .content("영업")
                .build();

        ProfileColumnDictionary pcd_skillMainCategory_5 = ProfileColumnDictionary.builder()
                .profileColumn(skillMainCategory)
                .content("마케팅")
                .build();

        ProfileColumnDictionary pcd_skillMainCategory_6 = ProfileColumnDictionary.builder()
                .profileColumn(skillMainCategory)
                .content("구매")
                .build();

        ProfileColumnDictionary pcd_skillMainCategory_7 = ProfileColumnDictionary.builder()
                .profileColumn(skillMainCategory)
                .content("SCM")
                .build();

        ProfileColumnDictionary pcd_skillMainCategory_8 = ProfileColumnDictionary.builder()
                .profileColumn(skillMainCategory)
                .content("물류")
                .build();

        ProfileColumnDictionary pcd_skillMainCategory_9 = ProfileColumnDictionary.builder()
                .profileColumn(skillMainCategory)
                .content("환경안전")
                .build();

        ProfileColumnDictionary pcd_skillMainCategory_10 = ProfileColumnDictionary.builder()
                .profileColumn(skillMainCategory)
                .content("품질/서비스")
                .build();

        ProfileColumnDictionary pcd_skillMainCategory_11 = ProfileColumnDictionary.builder()
                .profileColumn(skillMainCategory)
                .content("법무")
                .build();

        ProfileColumnDictionary pcd_skillMainCategory_12 = ProfileColumnDictionary.builder()
                .profileColumn(skillMainCategory)
                .content("재무")
                .build();

        ProfileColumnDictionary pcd_skillMainCategory_13 = ProfileColumnDictionary.builder()
                .profileColumn(skillMainCategory)
                .content("대외협력")
                .build();

        ProfileColumnDictionary pcd_skillMainCategory_14 = ProfileColumnDictionary.builder()
                .profileColumn(skillMainCategory)
                .content("경영관리")
                .build();

// "skillSubCategory" column
        ProfileColumnDictionary pcd_skillSubCategory_1 = ProfileColumnDictionary.builder()
                .profileColumn(skillSubCategory)
                .content("AI(기계학습, Core)")
                .build();

        ProfileColumnDictionary pcd_skillSubCategory_2 = ProfileColumnDictionary.builder()
                .profileColumn(skillSubCategory)
                .content("AI(비전, 영상)")
                .build();

        ProfileColumnDictionary pcd_skillSubCategory_3 = ProfileColumnDictionary.builder()
                .profileColumn(skillSubCategory)
                .content("AI(음성, 번역)")
                .build();

        ProfileColumnDictionary pcd_skillSubCategory_4 = ProfileColumnDictionary.builder()
                .profileColumn(skillSubCategory)
                .content("AI(Data)")
                .build();

        ProfileColumnDictionary pcd_skillSubCategory_5 = ProfileColumnDictionary.builder()
                .profileColumn(skillSubCategory)
                .content("AI(로봇)")
                .build();

        ProfileColumnDictionary pcd_skillSubCategory_6 = ProfileColumnDictionary.builder()
                .profileColumn(skillSubCategory)
                .content("5G/6G")
                .build();

// "isF" column
        ProfileColumnDictionary pcd_isF_1 = ProfileColumnDictionary.builder()
                .profileColumn(isF)
                .content("Y")
                .build();

        ProfileColumnDictionary pcd_isF_2 = ProfileColumnDictionary.builder()
                .profileColumn(isF)
                .content("N")
                .build();

        ProfileColumnDictionary pcd_isS_1 = ProfileColumnDictionary.builder()
                .profileColumn(isS)
                .content("Y")
                .build();

        ProfileColumnDictionary pcd_isS_2 = ProfileColumnDictionary.builder()
                .profileColumn(isS)
                .content("N")
                .build();

        List<ProfileColumnDictionary> allProfileColumnDictionaries = Arrays.asList(
                pcd_visa_1, pcd_visa_2, pcd_visa_3,
                pcd_korean_1, pcd_korean_2, pcd_korean_3, pcd_korean_4,
                pcd_relocation_1, pcd_relocation_2, pcd_relocation_3, pcd_relocation_4,
                pcd_foundPath_1, pcd_foundPath_2, pcd_foundPath_3, pcd_foundPath_4, pcd_foundPath_5,
                pcd_foundPath_6, pcd_foundPath_7, pcd_foundPath_8, pcd_foundPath_9,
                pcd_skillMainCategory_1, pcd_skillMainCategory_2, pcd_skillMainCategory_3, pcd_skillMainCategory_4,
                pcd_skillMainCategory_5, pcd_skillMainCategory_6, pcd_skillMainCategory_7, pcd_skillMainCategory_8,
                pcd_skillMainCategory_9, pcd_skillMainCategory_10, pcd_skillMainCategory_11, pcd_skillMainCategory_12,
                pcd_skillMainCategory_13, pcd_skillMainCategory_14,
                pcd_skillSubCategory_1, pcd_skillSubCategory_2, pcd_skillSubCategory_3, pcd_skillSubCategory_4,
                pcd_skillSubCategory_5, pcd_skillSubCategory_6,
                pcd_isF_1, pcd_isF_2, pcd_isS_1, pcd_isS_2
        );

        profileColumnDictionaryJpaRepository.saveAll(allProfileColumnDictionaries);
    }
}

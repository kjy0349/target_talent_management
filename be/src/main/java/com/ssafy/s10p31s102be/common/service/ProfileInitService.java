package com.ssafy.s10p31s102be.common.service;

import com.ssafy.s10p31s102be.common.infra.entity.Keyword;
import com.ssafy.s10p31s102be.common.infra.enums.KeywordType;
import com.ssafy.s10p31s102be.member.infra.entity.*;
import com.ssafy.s10p31s102be.member.infra.repository.*;
import com.ssafy.s10p31s102be.networking.infra.repository.ExecutiveJpaRepository;
import com.ssafy.s10p31s102be.profile.infra.entity.*;
import com.ssafy.s10p31s102be.profile.infra.entity.career.Career;
import com.ssafy.s10p31s102be.profile.infra.entity.career.Company;
import com.ssafy.s10p31s102be.profile.infra.entity.education.Education;
import com.ssafy.s10p31s102be.profile.infra.entity.education.School;
import com.ssafy.s10p31s102be.profile.infra.enums.Degree;
import com.ssafy.s10p31s102be.profile.infra.enums.EmployType;
import com.ssafy.s10p31s102be.profile.infra.enums.ProfileColumnDataType;
import com.ssafy.s10p31s102be.profile.infra.repository.CountryJpaRepository;
import com.ssafy.s10p31s102be.profile.infra.repository.ProfileColumnJpaRepository;
import com.ssafy.s10p31s102be.profile.infra.repository.ProfileJpaRepository;
import com.ssafy.s10p31s102be.project.infra.entity.Project;
import com.ssafy.s10p31s102be.project.infra.entity.ProjectProfile;
import com.ssafy.s10p31s102be.project.infra.repository.ProjectJpaRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileInitService {
    private final ProfileJpaRepository profileRepository;

    private final MemberJpaRepository memberRepository;

    private final DepartmentJpaRepository departmentRepository;

    private final ProjectJpaRepository projectRepository;

    private final ProfileColumnJpaRepository profileColumnRepository;

    private final JobRankJpaRepository jobRankRepository;

    private final ExecutiveJpaRepository executiveRepository;

    private final CountryJpaRepository countryRepository;

    private final AuthorityJpaRepository authorityRepository;

    private final TeamJpaRepository teamRepository;

    public Profile initProfileDummyDatas(Integer profileId) {
        // given
        Authority authority = Authority.builder()
                .authName("채용담당자")
                .authLevel(3)
                .build();

        authorityRepository.save(authority);

        Team team = Team.builder()
                .name("People팀")
                .description("People")
                .build();

        teamRepository.save(team);

        // 발굴 담당자 생성
        Department department = new Department("DX" +  + profileId, "테스트 DX" +  + profileId + " 부서입니다.");
        departmentRepository.save(department);
        Member member = Member.builder()
                .name("테스트 발굴 담당자" + profileId)
                .department(department)
                .authority(authority)
                .team(team)
                .knoxId("test@test.com")
                .password("1234")
                .build();
        memberRepository.save(member);
        // 타겟직급 생성
        JobRank jobRank = JobRank
                .builder()
                .description("직급1")
                .build();
        jobRankRepository.save(jobRank);
        // profile 생성
        Profile mockProfile = Profile.builder()
                .founder(member.getName())
                .profileImage("image")
                .build();
        mockProfile.updateJobRank(jobRank);

        // project 생성
        Project mockProject1 = Project.builder()
                .title("테스트 프로젝트1")
                .build();
        Project mockProject2 = Project.builder()
                .title("테스트 프로젝트2")
                .build();
        projectRepository.save(mockProject1);
        projectRepository.save(mockProject2);
        ProjectProfile mockProjectProfile1 = new ProjectProfile(mockProject1, mockProfile);
        ProjectProfile mockProjectProfile2 = new ProjectProfile(mockProject2, mockProfile);
        mockProfile.getProjectProfiles().add(mockProjectProfile1);
        mockProfile.getProjectProfiles().add(mockProjectProfile2);

        // 국가 생성
        Country country = Country.builder()
                .name("한국")
                .code("KR")
                .continent("Asia")
                .build();

        countryRepository.save(country);

        // 회사 생성
        Company company = Company.builder()
                .name("삼성전자")
                .build();

        // 경력 생성
        Career career1 = Career.builder()
                .role("테스트 담당업무 1")
                .jobRank("테스트 직급 1")
                .startedAt(LocalDateTime.parse("2024-04-01T23:59:59.999"))
                .endedAt(LocalDateTime.parse("2024-04-24T23:59:59.999"))
                .careerPeriodMonth(ChronoUnit.MONTHS.between(LocalDateTime.parse("2024-04-01T23:59:59.999"), LocalDateTime.parse("2024-04-24T23:59:59.999")))
                .employType(EmployType.FULL_TIME)
                .isManager(false)
                .isCurrent(true)
                .dept("테스트 부서 1")
                .company(company)
                .profile(mockProfile)
                .build();
        Career career2 = Career.builder()
                .role("테스트 담당업무 2")
                .jobRank("테스트 직급 2")
                .startedAt(LocalDateTime.parse("2023-04-01T23:59:59.999"))
                .endedAt(LocalDateTime.parse("2023-04-24T23:59:59.999"))
                .careerPeriodMonth(ChronoUnit.MONTHS.between(LocalDateTime.parse("2024-04-01T23:59:59.999"), LocalDateTime.parse("2024-04-24T23:59:59.999")))
                .employType(EmployType.CONTRACT)
                .isManager(false)
                .isCurrent(false)
                .dept("테스트 부서 2")
                .company(company)
                .profile(mockProfile)
                .build();
        mockProfile.getCareers().add(career1);
        mockProfile.getCareers().add(career2);

        // 학력 생성
        School school1 = new School("테스트 학교 1", "한국");
        Education education1 = Education.builder()
                .school(school1)
                .major("테스트 학과 1")
                .degree(Degree.BACHELOR)
                .enteredAt(LocalDateTime.parse("2023-04-01T23:59:59.999"))
                .graduatedAt(LocalDateTime.parse("2023-04-24T23:59:59.999"))
                .profile(mockProfile)
                .build();

        School school2 = new School("테스트 학교 2", "한국");
        Education education2 = Education.builder()
                .school(school2)
                .major("테스트 학과 2")
                .degree(Degree.BACHELOR)
                .enteredAt(LocalDateTime.parse("2024-04-01T23:59:59.999"))
                .graduatedAt(LocalDateTime.parse("2024-04-24T23:59:59.999"))
                .profile(mockProfile)
                .build();
        mockProfile.getEducations().add(education1);
        mockProfile.getEducations().add(education2);

        // special(키워드) 생성
        Keyword keyword1 = new Keyword(KeywordType.PROFILE, "테스트 기술" + profileId + UUID.randomUUID());
        Keyword keyword2 = new Keyword(KeywordType.PROFILE, "테스트 기술" + profileId + UUID.randomUUID());
        ProfileKeyword profileKeyword1 = new ProfileKeyword(mockProfile, keyword1);
        ProfileKeyword profileKeyword2 = new ProfileKeyword(mockProfile, keyword2);

        mockProfile.getProfileKeywords().add(profileKeyword1);
        mockProfile.getProfileKeywords().add(profileKeyword2);

        // 프로필컬럼 동적 생성
        ProfileColumn profileColumn1 = profileColumnRepository.findByName("name")
                .orElseGet(() -> {
                    ProfileColumn newColumn = ProfileColumn.builder()
                            .name("name")
                            .label("이름")
                            .dataType(ProfileColumnDataType.STRING)
                            .required(true)
                            .isPreview(true)
                            .build();
                    profileColumnRepository.saveAndFlush(newColumn);
                    return newColumn;
                });
        ProfileColumn profileColumn2 = profileColumnRepository.findByName("column1")
                .orElseGet(() -> {
                    ProfileColumn newColumn = ProfileColumn.builder()
                            .name("column1")
                            .label("column1")
                            .dataType(ProfileColumnDataType.STRING)
                            .required(true)
                            .isPreview(true)
                            .build();
                    profileColumnRepository.saveAndFlush(newColumn);
                    return newColumn;
                });
        ProfileColumn profileColumn3 = profileColumnRepository.findByName("column1")
                .orElseGet(() -> {
                    ProfileColumn newColumn = ProfileColumn.builder()
                            .name("column1")
                            .label("column1")
                            .dataType(ProfileColumnDataType.STRING)
                            .required(true)
                            .isPreview(true)
                            .build();
                    profileColumnRepository.saveAndFlush(newColumn);
                    return newColumn;
                });
        ProfileColumnData profileColumnData1 = ProfileColumnData.builder()
                .profile(mockProfile)
                .profileColumn(profileColumn1)
                .content("테스트 프로필 이름" + profileId)
                .build();
        ProfileColumnData profileColumnData2 = ProfileColumnData.builder()
                .profile(mockProfile)
                .profileColumn(profileColumn2)
                .content("한국")
                .build();
        ProfileColumnData profileColumnData3 = ProfileColumnData.builder()
                .profile(mockProfile)
                .profileColumn(profileColumn3)
                .content("1997-12-19")
                .build();
        List<ProfileColumnData> profileColumnDatas = mockProfile.getProfileColumnDatas();
        profileColumnDatas.add(profileColumnData1);
        profileColumnDatas.add(profileColumnData2);
        profileColumnDatas.add(profileColumnData3);
        profileRepository.save(mockProfile);

        // 직급4 생성
        /*
                this.name = name;
        this.department = department;
        this.jobRank = jobRank;
        this.email = email;
         */
        Executive executive = Executive.builder()
                .name("직급41")
                .department("DX")
                .jobRank("직급4")
                .email("직급41@samsung.com")
                .build();
        Executive executive2 = Executive.builder()
                .name("직급42")
                .department("DX")
                .jobRank("직급4")
                .email("직급42@samsung.com")
                .build();
        executiveRepository.save(executive);
        executiveRepository.save(executive2);
        return mockProfile;
    }
}

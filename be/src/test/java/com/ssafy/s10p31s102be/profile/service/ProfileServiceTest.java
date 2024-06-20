package com.ssafy.s10p31s102be.profile.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.s10p31s102be.common.service.ProfileInitService;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.member.infra.repository.MemberJpaRepository;
import com.ssafy.s10p31s102be.profile.dto.request.ProfileCreateDto;
import com.ssafy.s10p31s102be.profile.dto.request.ProfileFilterSearchDto;
import com.ssafy.s10p31s102be.profile.dto.response.CareerPreviewDto;
import com.ssafy.s10p31s102be.profile.dto.response.EducationPreviewDto;
import com.ssafy.s10p31s102be.profile.dto.response.KeywordPreviewDto;
import com.ssafy.s10p31s102be.profile.dto.response.ProfileDetailDto;
import com.ssafy.s10p31s102be.profile.dto.response.ProfilePreviewDto;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.enums.Degree;
import com.ssafy.s10p31s102be.profile.infra.repository.ProfileColumnJpaRepository;
import com.ssafy.s10p31s102be.profile.infra.enums.EmployStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@SpringBootTest
@Transactional
class ProfileServiceTest {

    @Autowired
    private ProfileServiceImpl profileService;

    @Autowired
    ProfileInitService profileInitService;

    @Autowired
    ProfileColumnJpaRepository profileColumnJpaRepository;

    @Autowired
    MemberJpaRepository memberJpaRepository;

    Profile mockProfile;

    @PersistenceContext
    EntityManager em;

    JPAQueryFactory queryFactory;
    @BeforeEach
    void 초기_프로필_데이터_생성() {
        mockProfile = profileInitService.initProfileDummyDatas(1);
        for (int i = 2; i <= 10; i++) {
            profileInitService.initProfileDummyDatas(i);
        }
    }

    @Test
    void 프로필_데이터를_동적_컬럼과_등록할_수_있다() {

        Map<String, String> mockColumns = new HashMap<>();

        profileColumnJpaRepository.findAllByRequiredTrue().forEach(pc -> {
            mockColumns.put(pc.getName(), pc.getName());
        });

        ProfileCreateDto dto = ProfileCreateDto.builder()
                .profileImage("localhost:8080")
                .columns(mockColumns)
                .build();

        Profile createdProfile = profileService.create(mockProfile.getManager().getId(), dto);

        assertThat(createdProfile).isNotNull();
        assertThat(dto.getColumns().size()).isEqualTo(createdProfile.getProfileColumnDatas().size());
    }

    @Test
    void 등록된_프로필의_프리뷰_데이터를_조회할_수_있다 () throws Exception {
        // given
        // when
        ProfilePreviewDto profilePreviewDto = profileService.getProfilePreviewById(mockProfile.getId());
        Map<String, String> columnDatas = profilePreviewDto.getColumnDatas();
        List<CareerPreviewDto> careerPreviews = profilePreviewDto.getCareersPreview();
        List<EducationPreviewDto> educationPreviews = profilePreviewDto.getEducationsPreview();
        List<KeywordPreviewDto> keywordPreviews = profilePreviewDto.getKeywordsPreview();

        // then
        assertThat(columnDatas.get("column1")).isEqualTo("한국");
        assertThat(columnDatas.get("name")).isEqualTo("테스트 프로필 이름1");
        assertThat(columnDatas.get("column1")).isEqualTo("1997-12-19");

        assertThat(careerPreviews.get(0).getCompanyName()).isEqualTo("삼성전자");
        assertThat(careerPreviews.get(0).getRole()).isEqualTo("테스트 담당업무 1");
        assertThat(careerPreviews.get(0).getStartedAt()).isEqualTo(LocalDateTime.parse("2024-04-01T23:59:59.999"));
        assertThat(careerPreviews.get(0).getEndedAt()).isEqualTo(LocalDateTime.parse("2024-04-24T23:59:59.999"));

        assertThat(careerPreviews.get(1).getCompanyName()).isEqualTo("삼성전자");
        assertThat(careerPreviews.get(1).getRole()).isEqualTo("테스트 담당업무 2");
        assertThat(careerPreviews.get(1).getStartedAt()).isEqualTo(LocalDateTime.parse("2023-04-01T23:59:59.999"));
        assertThat(careerPreviews.get(1).getEndedAt()).isEqualTo(LocalDateTime.parse("2023-04-24T23:59:59.999"));


        assertThat(educationPreviews.get(0).getSchoolName()).isEqualTo("테스트 학교 1");
        assertThat(educationPreviews.get(0).getMajor()).isEqualTo("테스트 학과 1");
        assertThat(educationPreviews.get(0).getEnteredAt()).isEqualTo(LocalDateTime.parse("2023-04-01T23:59:59.999"));
        assertThat(educationPreviews.get(0).getGraduatedAt()).isEqualTo(LocalDateTime.parse("2023-04-24T23:59:59.999"));

        assertThat(educationPreviews.get(1).getSchoolName()).isEqualTo("테스트 학교 2");
        assertThat(educationPreviews.get(1).getMajor()).isEqualTo("테스트 학과 2");
        assertThat(educationPreviews.get(1).getEnteredAt()).isEqualTo(LocalDateTime.parse("2024-04-01T23:59:59.999"));
        assertThat(educationPreviews.get(1).getGraduatedAt()).isEqualTo(LocalDateTime.parse("2024-04-24T23:59:59.999"));

        assertThat(keywordPreviews.size()).isEqualTo(2);

        assertThat(profilePreviewDto.getMemberPreview().getDepartmentName()).isEqualTo("DX1");
        assertThat(profilePreviewDto.getMemberPreview().getName()).isEqualTo("테스트 발굴 담당자1");
    }

    @Test
    void 프로필ID를_이용해_프로필_상세사항을_조회할_수_있다() {
        Integer profileId = mockProfile.getId();
        ProfileDetailDto profileDetail = profileService.getProfileDetailById(null, profileId);

        assertThat(profileDetail.getProjectsPreview().size()).isEqualTo(2);
        assertThat(profileDetail.getCareersDetail().size()).isEqualTo(2);
        assertThat(profileDetail.getEducationsDetail().size()).isEqualTo(2);
        assertThat(profileDetail.getKeywordsPreview().size()).isEqualTo(2);
        assertThat(profileDetail.getMemberPreview().getName()).isEqualTo("테스트 발굴 담당자1");
    }
}
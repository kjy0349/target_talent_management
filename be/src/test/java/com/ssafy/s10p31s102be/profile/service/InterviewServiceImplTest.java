package com.ssafy.s10p31s102be.profile.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ssafy.s10p31s102be.member.infra.entity.Executive;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.member.infra.repository.MemberJpaRepository;
import com.ssafy.s10p31s102be.networking.infra.repository.ExecutiveJpaRepository;
import com.ssafy.s10p31s102be.profile.dto.request.InterviewCreateDto;
import com.ssafy.s10p31s102be.profile.dto.request.InterviewResultCreateDto;
import com.ssafy.s10p31s102be.profile.dto.request.InterviewUpdateDto;
import com.ssafy.s10p31s102be.profile.exception.InterviewNotFoundException;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.entity.community.Interview;
import com.ssafy.s10p31s102be.profile.infra.entity.community.InterviewResult;
import com.ssafy.s10p31s102be.profile.infra.enums.InterviewResultType;
import com.ssafy.s10p31s102be.profile.infra.enums.InterviewType;
import com.ssafy.s10p31s102be.profile.infra.repository.InterviewJpaRepository;
import com.ssafy.s10p31s102be.profile.infra.repository.ProfileJpaRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class InterviewServiceImplTest {

    @Autowired
    private MemberJpaRepository memberRepository;

    @Autowired
    private InterviewJpaRepository interviewRepository;

    @Autowired
    private ProfileJpaRepository profileRepository;

    @Autowired
    private ExecutiveJpaRepository executiveRepository;

    @Autowired
    private InterviewService interviewService;

    private Profile profile;
    private Profile profile2;
    private Interview interview;
    private Executive executive;
    private Executive executive2;
    private List<InterviewResult> interviewResults;

    @BeforeEach
    void 테스트_전_데이터_삽입() {
        Member member = new Member();
        memberRepository.save(member);

        profile = Profile.builder()
                .manager(member)
                .build();
        profile2 = Profile.builder()
                .manager(member)
                .build();
        profileRepository.save(profile);
        profileRepository.save(profile2);

        executive = new Executive("테스트 직급41", "DX1", "직급4", "test1@samsung.com");
        executive2 = new Executive("테스트 직급42", "DX1", "직급4", "test2@samsung.com");
        executiveRepository.save(executive);
        executiveRepository.save(executive2);

        interview = Interview.builder()
                .profile(profile)
                .interviewDegree(5)
                .meetDate(LocalDateTime.now())
                .interviewType(InterviewType.FACE)
                .place("서울")
                .description("입사 전 인터뷰")
                .build();
        InterviewResult interviewResult = InterviewResult.builder()
                .interviewResultType(InterviewResultType.A_PLUS)
                .interview(interview)
                .executive(executive)
                .build();
        InterviewResult interviewResult2 = InterviewResult.builder()
                .interviewResultType(InterviewResultType.A_PLUS)
                .interview(interview)
                .executive(executive2)
                .build();
        interview.getInterviewResults().add(interviewResult);
        interview.getInterviewResults().add(interviewResult2);
        interviewResults = new ArrayList<>();
        interviewResults.add(interviewResult);
        interviewResults.add(interviewResult2);
        interviewRepository.save(interview);
    }

    @Test
    void 사용자는_인터뷰를_등록할_수_있다() {
        // given
        LocalDateTime mockTime = LocalDateTime.now();
        InterviewCreateDto interviewCreateDto = InterviewCreateDto.builder()
                .interviewDegree(3)
                .meetDate(mockTime)
                .interviewType(InterviewType.NO_FACE)
                .place("서울")
                .description("입사 전 인터뷰")
                .interviewResults(new ArrayList<>(List.of(
                        InterviewResultCreateDto.builder()
                                .interviewResultType(InterviewResultType.A_PLUS)
                                .executiveId(executive.getId())
                                .build(),
                        InterviewResultCreateDto.builder()
                                .interviewResultType(InterviewResultType.A_PLUS)
                                .executiveId(executive2.getId())
                                .build()
                )))
                .build();

        // when
        Interview createdInterview = interviewService.create(profile.getId(), interviewCreateDto);

        // then
        assertThat(createdInterview).isNotNull();
        assertThat(createdInterview.getPlace()).isEqualTo(interviewCreateDto.getPlace());
        assertThat(createdInterview.getDescription()).isEqualTo(interviewCreateDto.getDescription());
        assertThat(createdInterview.getInterviewType()).isEqualTo(interviewCreateDto.getInterviewType());
        assertThat(createdInterview.getMeetDate()).isEqualTo(interviewCreateDto.getMeetDate());
        assertThat(createdInterview.getProfile()).isEqualTo(profile);
        assertThat(createdInterview.getInterviewDegree()).isEqualTo(interviewCreateDto.getInterviewDegree());
        for (int i = 0; i < interviewCreateDto.getInterviewResults().size(); i++) {
            assertThat(createdInterview.getInterviewResults().get(i).getExecutive().getId())
                    .isEqualTo(interviewCreateDto.getInterviewResults().get(i).getExecutiveId());
            assertThat(createdInterview.getInterviewResults().get(i).getInterviewResultType())
                    .isEqualTo(interviewCreateDto.getInterviewResults().get(i).getInterviewResultType());
        }
    }

    @Test
    void 사용자는_기존_인터뷰를_업데이트할_수_있다() {
        // given
        LocalDateTime mockTime = LocalDateTime.now();
        InterviewUpdateDto interviewUpdateDto = InterviewUpdateDto.builder()
                .interviewDegree(6)
                .meetDate(mockTime)
                .interviewType(InterviewType.FACE)
                .place("인천")
                .description("채용 직전 면접")
                .interviewResults(new ArrayList<>(List.of(
                        InterviewResultCreateDto.builder()
                                .interviewResultType(InterviewResultType.B_PLUS)
                                .executiveId(executive.getId())
                                .build(),
                        InterviewResultCreateDto.builder()
                                .interviewResultType(InterviewResultType.C_PLUS)
                                .executiveId(executive2.getId())
                                .build()
                )))
                .build();


        // when
        Interview updatedInterview = interviewService.update(interview.getId(), interviewUpdateDto);

        // then
        assertThat(updatedInterview).isNotNull();
        assertThat(updatedInterview.getPlace()).isEqualTo(interviewUpdateDto.getPlace());
        assertThat(updatedInterview.getDescription()).isEqualTo(interviewUpdateDto.getDescription());
        assertThat(updatedInterview.getInterviewType()).isEqualTo(interviewUpdateDto.getInterviewType());
        assertThat(updatedInterview.getMeetDate()).isEqualTo(interviewUpdateDto.getMeetDate());
        assertThat(updatedInterview.getInterviewDegree()).isEqualTo(interviewUpdateDto.getInterviewDegree());
        for (int i = 0; i < interviewUpdateDto.getInterviewResults().size(); i++) {
            assertThat(updatedInterview.getInterviewResults().get(i).getExecutive().getId())
                    .isEqualTo(interviewUpdateDto.getInterviewResults().get(i).getExecutiveId());
            assertThat(updatedInterview.getInterviewResults().get(i).getInterviewResultType())
                    .isEqualTo(interviewUpdateDto.getInterviewResults().get(i).getInterviewResultType());
        }
    }

    @Test
    void 사용자는_면접기록을_삭제할_수_있다() {
        // given

        // when
        interviewService.delete(1);

        // then
        assertThatThrownBy(() -> {
            interviewRepository.findById(1).orElseThrow(() -> new InterviewNotFoundException(1, this));
        }).isInstanceOf(InterviewNotFoundException.class);
    }
}
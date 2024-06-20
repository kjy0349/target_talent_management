package com.ssafy.s10p31s102be.profile.service;

import com.ssafy.s10p31s102be.profile.dto.request.EmploymentApproveCreateDto;
import com.ssafy.s10p31s102be.profile.dto.request.EmploymentApproveUpdateDto;
import com.ssafy.s10p31s102be.profile.exception.SameEmploymentApproveStepFoundException;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.entity.employment.EmploymentApprove;
import com.ssafy.s10p31s102be.profile.infra.enums.EmploymentApproveResultType;
import com.ssafy.s10p31s102be.profile.infra.repository.EmploymentApproveJpaRepository;
import com.ssafy.s10p31s102be.profile.infra.repository.ProfileJpaRepository;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings
class EmploymentApproveServiceImplTest {
    @Mock
    EmploymentApproveJpaRepository employmentApproveRepository;
    @Mock
    ProfileJpaRepository profileRepository;
    @InjectMocks
    EmploymentApproveServiceImpl employmentApproveService;
    EmploymentApprove mockEmploymentApprove;
    EmploymentApprove mockEmploymentApprove2;
    Profile mockProfile;

    @BeforeEach
    void 초기_데이터_세팅() throws Exception{
        mockProfile = Profile.builder().build();
        Field id = mockProfile.getClass().getDeclaredField("id");
        id.setAccessible(true);
        id.set(mockProfile, 1);
        mockEmploymentApprove = EmploymentApprove.builder()
                .step(1)
                .approvedAt(LocalDateTime.now())
                .description("테스트용 APPROVE_E1 입니다.")
                .resultType(EmploymentApproveResultType.COMPLETED)
                .profile(mockProfile)
                .build();
        Field id1 = mockEmploymentApprove.getClass().getDeclaredField("id");
        id1.setAccessible(true);
        id1.set(mockEmploymentApprove, 1L);
        mockEmploymentApprove2 = EmploymentApprove.builder()
                .step(2)
                .approvedAt(LocalDateTime.now())
                .description("테스트용 APPROVE_E2 입니다.")
                .resultType(EmploymentApproveResultType.UNCOMPLETED)
                .profile(mockProfile)
                .build();
        Field id2 = mockEmploymentApprove.getClass().getDeclaredField("id");
        id2.setAccessible(true);
        id2.set(mockEmploymentApprove, 2L);
    }

    @Test
    void 사용자는_프로필에_APPROVE_E을_생성할_수_있다() {
        // given
        EmploymentApproveCreateDto employmentApproveCreateDto = EmploymentApproveCreateDto.builder()
                .step(mockEmploymentApprove.getStep())
                .approvedAt(mockEmploymentApprove.getApprovedAt())
                .description(mockEmploymentApprove.getDescription())
                .resultType(EmploymentApproveResultType.COMPLETED)
                .build();

        // when
        when(employmentApproveRepository.save(any(EmploymentApprove.class))).thenReturn(mockEmploymentApprove);
        when(profileRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(mockProfile));

        EmploymentApprove employmentApprove = employmentApproveService.create(mockProfile.getId(), employmentApproveCreateDto);

        // then
        verify(employmentApproveRepository, times(1)).save(any(EmploymentApprove.class));
        assertThat(employmentApprove.getProfile()).isEqualTo(mockProfile);
    }

    @Test
    void 사용자는_프로필_ID를_이용해_APPROVE_E_데이터들을_조회할_수_있다() {
        // given
        // when
        when(employmentApproveRepository.findAllByProfileId(mockProfile.getId())).thenReturn(List.of(mockEmploymentApprove, mockEmploymentApprove2));
        List<EmploymentApprove> employmentApproves = employmentApproveService.readAllEmploymentApproveByProfileId(mockProfile.getId());

        // then
        verify(employmentApproveRepository).findAllByProfileId(mockProfile.getId());
        assertThat(employmentApproves.size()).isEqualTo(2);
    }

    @Test
    void 사용자는_APPROVE_E_ID를_이용해_APPROVE_E을_수정할_수_있다() {
        // given
        LocalDateTime now = LocalDateTime.now();
        // when
        when(employmentApproveRepository.findById(any())).thenReturn(Optional.of(mockEmploymentApprove));
        employmentApproveService.update(1L, EmploymentApproveUpdateDto.builder()
                        .approvedAt(now)
                        .description("테스트 APPROVE_E 이유")
                        .step(3)
                        .resultType(EmploymentApproveResultType.COMPLETED)
                .build());
        // then
        assertThat(mockEmploymentApprove.getApprovedAt()).isEqualTo(now);
        assertThat(mockEmploymentApprove.getDescription()).isEqualTo("테스트 APPROVE_E 이유");
        assertThat(mockEmploymentApprove.getStep()).isEqualTo(3);
        assertThat(mockEmploymentApprove.getResultType()).isEqualTo(EmploymentApproveResultType.COMPLETED);
    }

    @Test
    void 사용자는_APPROVE_E_ID를_이용해_APPROVE_E을_삭제할_수_있다() {
        // given
        // when
        employmentApproveService.delete(1L);
        // then
        verify(employmentApproveRepository, times(1)).deleteById(1L);
    }

    @Test
    void 프로필_내_동일한_step_의_APPROVE_E을_등록하려고하면_SameEmploymentApproveFoundException_이_발생한다() {
        // given
        EmploymentApproveCreateDto employmentApproveCreateDto = EmploymentApproveCreateDto.builder()
                .step(mockEmploymentApprove.getStep())
                .approvedAt(mockEmploymentApprove.getApprovedAt())
                .description(mockEmploymentApprove.getDescription())
                .resultType(EmploymentApproveResultType.COMPLETED)
                .build();

        // when
        when(employmentApproveRepository.findByStepAndProfileId(employmentApproveCreateDto.getStep(), mockProfile.getId())).thenReturn(Optional.of(mockEmploymentApprove));

        // then
        assertThatThrownBy(() -> employmentApproveService.create(mockProfile.getId(), employmentApproveCreateDto))
                .isInstanceOf(SameEmploymentApproveStepFoundException.class);
    }
}
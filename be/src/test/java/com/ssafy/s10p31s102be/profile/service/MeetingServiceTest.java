package com.ssafy.s10p31s102be.profile.service;


import com.ssafy.s10p31s102be.member.infra.repository.DepartmentJpaRepository;
import com.ssafy.s10p31s102be.profile.exception.ProfileNotFoundException;
import com.ssafy.s10p31s102be.member.infra.entity.Department;
import com.ssafy.s10p31s102be.member.infra.entity.JobRank;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.member.infra.repository.JobRankJpaRepository;
import com.ssafy.s10p31s102be.member.infra.repository.MemberJpaRepository;
import com.ssafy.s10p31s102be.profile.dto.request.MeetingCreateRequestDto;
import com.ssafy.s10p31s102be.profile.dto.request.MeetingUpdateRequestDto;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.entity.community.Meeting;
import com.ssafy.s10p31s102be.profile.infra.enums.MeetingType;
import com.ssafy.s10p31s102be.profile.infra.repository.MeetingJpaRepository;
import com.ssafy.s10p31s102be.profile.infra.repository.ProfileJpaRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MeetingServiceTest {
    @Mock
    MemberJpaRepository memberRepository;
    @Mock
    DepartmentJpaRepository departmentRepository;
    @Mock
    ProfileJpaRepository profileRepository;
    @Mock
    JobRankJpaRepository jobRankRepository;
    @Mock
    MeetingJpaRepository meetingRepository;

    @InjectMocks
    MeetingServiceImpl meetingService;

    private Member mockMember;
    private Department mockDepartment;
    private Profile mockProfile;
    private JobRank mockJobRank;

    @BeforeEach
    void 초기_면담_연관객체_등록하기() {
        mockMember = new Member();
        mockDepartment = new Department();
        mockProfile = new Profile();
        mockJobRank = new JobRank();
    }

    @Test
    void 사용자는_면담을_등록할_수_있다() {
        // given
        MeetingCreateRequestDto meetingCreateRequestDto = MeetingCreateRequestDto.builder()
//                .inChargeMemberId(1)
//                .departmentId(1)
//                .profileId(1)
//                .jobRankId(1)
//                .meetingType(MeetingType.RECRUITER)
                .build();

        when(memberRepository.findById(1)).thenReturn(Optional.of(mockMember));
        when(departmentRepository.findById(1)).thenReturn(Optional.of(mockDepartment));
        when(profileRepository.findById(1)).thenReturn(Optional.of(mockProfile));
        when(jobRankRepository.findById(1)).thenReturn(Optional.of(mockJobRank));
        // when
        Meeting result = meetingService.create(null, 1, meetingCreateRequestDto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getInChargeMember()).isEqualTo(mockMember);
        assertThat(result.getTargetDepartmentName()).isEqualTo(mockDepartment.getName());
        assertThat(result.getTargetJobRank()).isEqualTo(mockJobRank);
        assertThat(result.getProfile()).isEqualTo(mockProfile);

        verify(meetingRepository).save(result);
    }

    @Test
    void 존재하지_않는_프로필에_면담을_등록하려고하면_ProfileNotFoundException_이_발생한다() {
        // given
        MeetingCreateRequestDto meetingCreateRequestDto = MeetingCreateRequestDto.builder()
//                .inChargeMemberId(1)
//                .departmentId(1)
//                .profileId(2)
//                .jobRankId(1)
                .build();

        // when
        // 중간에 Exception이 던져지므로, findById가 수행되지 않는 객체가 있을 수도 있음.
        lenient().when(memberRepository.findById(1)).thenReturn(Optional.of(mockMember));
        lenient().when(departmentRepository.findById(1)).thenReturn(Optional.of(mockDepartment));
        lenient().when(profileRepository.findById(2)).thenReturn(Optional.empty());
        lenient().when(jobRankRepository.findById(1)).thenReturn(Optional.of(mockJobRank));

        // then
        assertThatThrownBy(() -> {
            meetingService.create(null, 1, meetingCreateRequestDto);
        }).isInstanceOf(ProfileNotFoundException.class);
    }

    @Test
    void 면담_ID로_면담을_찾을_수_있다 () throws Exception {
        // given
        Integer meetingId = 1;
        Meeting mockMeeting = new Meeting();

        when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(mockMeeting));

        // when
        // then
        assertThat(meetingRepository.findById(meetingId).isPresent()).isTrue();
        assertThat(meetingRepository.findById(meetingId).get()).isEqualTo(mockMeeting);
    }

    @Test
    void 프로필에_속한_면담들을_조회할_수_있다() {
        // given
        Integer profileId = 1;
        List<Meeting> expectedMeetings = Collections.singletonList(
                Meeting.builder().profile(mockProfile).build()
        );

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(mockProfile));
        when(meetingRepository.findMeetingsByProfileId(mockProfile.getId()))
                .thenReturn(expectedMeetings);

        // when
        List<Meeting> meetings = meetingService.readMeetings(null, profileId);

        // then
        assertThat(meetings).isEqualTo(expectedMeetings);
    }

    @Test
    void 사용자는_면담을_수정할_수_있다(){
        // given
        Integer meetingId = 1;
        Meeting mockMeeting = Meeting.builder()
                .build();
        MeetingCreateRequestDto meetingUpdateRequestDto = MeetingCreateRequestDto.builder()
//                .inChargeMemberId(1)
//                .departmentId(1)
//                .jobRankId(1)
                .build();

        when(memberRepository.findById(1)).thenReturn(Optional.of(mockMember));
        when(departmentRepository.findById(1)).thenReturn(Optional.of(mockDepartment));
        when(jobRankRepository.findById(1)).thenReturn(Optional.of(mockJobRank));
        when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(mockMeeting));

        // when
        Meeting meeting = meetingService.update(null, meetingId, meetingUpdateRequestDto);

        // then
        assertThat(meeting.getInChargeMember()).isEqualTo(mockMember);
        assertThat(meeting.getTargetDepartmentName()).isEqualTo(mockDepartment.getName());
        assertThat(meeting.getTargetJobRank()).isEqualTo(mockJobRank);
        assertThat(meeting).isEqualTo(mockMeeting);
    }

    @Test
    void 사용자는_면담을_삭제할_수_있다() {
        // given
        Integer meetingId = 1;
        Meeting mockMeeting = Meeting.builder().build();

        when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(mockMeeting));

        // when
        meetingService.delete(null, meetingId);

        // then
        verify(meetingRepository).delete(mockMeeting);
    }
}
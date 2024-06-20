package com.ssafy.s10p31s102be.profile.service;

import com.ssafy.s10p31s102be.common.util.AuthorityUtil;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.member.exception.DepartmentNotFoundException;
import com.ssafy.s10p31s102be.member.exception.ExecutiveNotFoundException;
import com.ssafy.s10p31s102be.member.exception.JobRankNotFoundException;
import com.ssafy.s10p31s102be.member.exception.MemberNotFoundException;
import com.ssafy.s10p31s102be.member.infra.entity.Department;
import com.ssafy.s10p31s102be.member.infra.entity.Executive;
import com.ssafy.s10p31s102be.member.infra.entity.JobRank;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.member.infra.repository.DepartmentJpaRepository;
import com.ssafy.s10p31s102be.member.infra.repository.JobRankJpaRepository;
import com.ssafy.s10p31s102be.member.infra.repository.MemberJpaRepository;
import com.ssafy.s10p31s102be.networking.infra.repository.ExecutiveJpaRepository;
import com.ssafy.s10p31s102be.profile.dto.request.MeetingCreateRequestDto;
import com.ssafy.s10p31s102be.profile.dto.request.MeetingUpdateRequestDto;
import com.ssafy.s10p31s102be.profile.exception.MeetingNotFoundException;
import com.ssafy.s10p31s102be.profile.exception.ProfileNotFoundException;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.entity.community.Meeting;
import com.ssafy.s10p31s102be.profile.infra.enums.MeetingType;
import com.ssafy.s10p31s102be.profile.infra.repository.MeetingJpaRepository;
import com.ssafy.s10p31s102be.profile.infra.repository.ProfileJpaRepository;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MeetingServiceImpl implements MeetingService{
    private final MeetingJpaRepository meetingRepository;
    private final MemberJpaRepository memberRepository;
    private final DepartmentJpaRepository departmentRepository;
    private final ProfileJpaRepository profileRepository;
    private final JobRankJpaRepository jobRankRepository;
    private final ExecutiveJpaRepository executiveRepository;

    @Override
    public Meeting create(UserDetailsImpl userDetails, Integer profileId, MeetingCreateRequestDto dto) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException(profileId, this));

        AuthorityUtil.validateAuthority(profile.getManager(), userDetails, 4);

        Meeting meeting = null;

        /*
            현업 / 채용부서장일 경우 간단한 면담 내용으로 변경
         */
        if (dto.getMeetingType() == MeetingType.RECRUITER) {
            meeting = Meeting.builder()
                    .meetingType(dto.getMeetingType())
                    .inChargeMember(dto.getInChargeMember())
                    .departmentName(dto.getTargetDepartment())
                    .targetJobRank(dto.getTargetJobRank())
                    .meetAt(dto.getMeetAt())
                    .isFace(dto.getIsFace())
                    .description(dto.getDescription())
                    .place(dto.getPlace())
                    .isMemberDirected(dto.getIsMemberDirected())
                    .currentTask(dto.getCurrentTask())
                    .leadershipDescription(dto.getLeadershipDescription())
                    .interestType(dto.getInterestType())
                    .interestTech(dto.getInterestTech())
                    .interestDescription(dto.getInterestDescription())
                    .question(dto.getQuestion())
                    .etc(dto.getEtc())
                    .country(dto.getCountry())
                    .isNetworking(false)
                    .participants(dto.getParticipants())
                    .profile(profile)
                    .member(memberRepository.findById(userDetails.getMemberId()).orElseThrow(() -> new MemberNotFoundException(userDetails.getMemberId(), this)))
                    .build();
        } else {
            meeting = Meeting.builder()
                    .meetingType(dto.getMeetingType())
                    .meetAt(dto.getMeetAt())
                    .isFace(dto.getIsFace())
                    .country(dto.getCountry())
                    .place(dto.getPlace())
                    .description(dto.getDescription())
                    .isNetworking(dto.getIsNetworking())
                    .participants(dto.getParticipants())
                    .profile(profile)
                    .member(memberRepository.findById(userDetails.getMemberId()).orElseThrow(() -> new MemberNotFoundException(userDetails.getMemberId(), this)))
                    .build();
        }

        return meetingRepository.save(meeting);
    }

    @Override
    public List<Meeting> readMeetings(UserDetailsImpl userDetails, Integer profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException(profileId, this));

        AuthorityUtil.validateAuthority(profile.getManager(), userDetails, 4);

        return meetingRepository.findMeetingsByProfileId(profile.getId());
    }

    @Override
    public Meeting findById(Integer meetingId) {
        return meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingNotFoundException(meetingId, this));
    }

    @Override
    public Meeting update(UserDetailsImpl userDetails, Integer meetingId, MeetingCreateRequestDto dto) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingNotFoundException(meetingId, this));

        AuthorityUtil.validateAuthority(meeting.getProfile().getManager(), userDetails, 4);

        meeting.update(dto);

        return meeting;
    }

    @Override
    public Meeting updateFavorite(UserDetailsImpl userDetails, Integer meetingId, Boolean isFavorite) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingNotFoundException(meetingId, this));

        AuthorityUtil.validateAuthority(meeting.getProfile().getManager(), userDetails, 4);

        meeting.updateFavorite(isFavorite);

        return meeting;
    }

    @Override
    public void delete(UserDetailsImpl userDetails, Integer meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingNotFoundException(meetingId, this));

        AuthorityUtil.validateAuthority(meeting.getProfile().getManager(), userDetails, 4);

        meetingRepository.delete(meeting);
    }
}

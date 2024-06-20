package com.ssafy.s10p31s102be.profile.service;

import com.ssafy.s10p31s102be.common.util.AuthorityUtil;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.member.exception.ExecutiveNotFoundException;
import com.ssafy.s10p31s102be.member.exception.MemberNotFoundException;
import com.ssafy.s10p31s102be.member.infra.repository.MemberJpaRepository;
import com.ssafy.s10p31s102be.networking.infra.repository.ExecutiveJpaRepository;
import com.ssafy.s10p31s102be.profile.dto.request.InterviewCreateDto;
import com.ssafy.s10p31s102be.profile.dto.request.InterviewUpdateDto;
import com.ssafy.s10p31s102be.profile.exception.InterviewNotFoundException;
import com.ssafy.s10p31s102be.profile.exception.ProfileNotFoundException;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.entity.community.Interview;
import com.ssafy.s10p31s102be.profile.infra.entity.community.InterviewResult;
import com.ssafy.s10p31s102be.profile.infra.repository.InterviewJpaRepository;
import com.ssafy.s10p31s102be.profile.infra.repository.InterviewResultJpaRepository;
import com.ssafy.s10p31s102be.profile.infra.repository.ProfileJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {
    private final InterviewJpaRepository interviewRepository;
    private final ProfileJpaRepository profileRepository;
    private final MemberJpaRepository memberRepository;
    private final InterviewResultJpaRepository interviewResultRepository;

    @Override
    public Interview create(UserDetailsImpl userDetails, Integer profileId, InterviewCreateDto dto) {

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException(profileId, this));

        AuthorityUtil.validateAuthority(profile.getManager(), userDetails, 5);

        Interview interview = Interview.builder()
                .interviewDegree(dto.getInterviewDegree())
                .meetDate(dto.getMeetDate())
                .interviewType(dto.getInterviewType())
                .place(dto.getPlace())
                .description(dto.getDescription())
                .profile(profile)
                .member(memberRepository.findById(userDetails.getMemberId()).orElseThrow(() -> new MemberNotFoundException(userDetails.getMemberId(), this)))
                .build();
        dto.getInterviewResults().forEach(interviewResultCreateDto -> {
            interview.getInterviewResults().add(InterviewResult.builder()
                    .interviewResultType(interviewResultCreateDto.getResult())
                    .interview(interview)
                    .executiveName(interviewResultCreateDto.getExecutiveName())
                    .build());
        });
        interviewRepository.save(interview);
        return interview;
    }

    @Override
    public Interview update(UserDetailsImpl userDetails, Integer interviewId, InterviewCreateDto dto) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new InterviewNotFoundException(interviewId, this));

        AuthorityUtil.validateAuthority(interview.getProfile().getManager(), userDetails, 5);

        interview.update(dto);

        interviewResultRepository.deleteAll(interview.getInterviewResults());
        interview.getInterviewResults().clear();

        dto.getInterviewResults().forEach(interviewResultCreateDto -> {
            interview.getInterviewResults().add(InterviewResult.builder()
                    .interviewResultType(interviewResultCreateDto.getResult())
                    .interview(interview)
                    .executiveName(interviewResultCreateDto.getExecutiveName())
                    .build());
        });
        return interview;
    }

    @Override
    public Interview updateFavorite(UserDetailsImpl userDetails, Integer interviewId, Boolean isFavorite) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new InterviewNotFoundException(interviewId, this));

        AuthorityUtil.validateAuthority(interview.getProfile().getManager(), userDetails, 5);

        interview.updateFavorite(isFavorite);

        return interview;
    }

    @Override
    public List<Interview> findAllInterviewByProfileId(UserDetailsImpl userDetails, Integer profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException(profileId, this));

        AuthorityUtil.validateAuthority(profile.getManager(), userDetails, 5);

        return interviewRepository.findAllByProfile(profile);
    }

    @Override
    public void delete(UserDetailsImpl userDetails, Integer interviewId) {
        Interview interview = interviewRepository.findById(interviewId).orElseThrow(() -> new InterviewNotFoundException(interviewId, this));

        AuthorityUtil.validateAuthority(interview.getProfile().getManager(), userDetails, 5);

        interviewRepository.delete(interview);
    }
}

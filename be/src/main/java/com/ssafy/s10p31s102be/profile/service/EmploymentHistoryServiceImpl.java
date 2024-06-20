package com.ssafy.s10p31s102be.profile.service;

import com.ssafy.s10p31s102be.common.exception.InvalidAuthorizationException;
import com.ssafy.s10p31s102be.common.util.AuthorityUtil;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.member.exception.MemberNotFoundException;
import com.ssafy.s10p31s102be.member.infra.repository.MemberJpaRepository;
import com.ssafy.s10p31s102be.profile.dto.request.EmploymentHistoryCreateDto;
import com.ssafy.s10p31s102be.profile.exception.EmploymentHistoryNotFoundException;
import com.ssafy.s10p31s102be.profile.exception.ProfileNotFoundException;
import com.ssafy.s10p31s102be.profile.infra.entity.EmploymentHistory;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.enums.EmploymentHistoryType;
import com.ssafy.s10p31s102be.profile.infra.repository.EmploymentHistoryJpaRepository;
import com.ssafy.s10p31s102be.profile.infra.repository.ProfileJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EmploymentHistoryServiceImpl implements EmploymentHistoryService {

    private final EmploymentHistoryJpaRepository employmentHistoryRepository;
    private final ProfileJpaRepository profileRepository;
    private final MemberJpaRepository memberRepository;

    @Override
    public EmploymentHistory create(UserDetailsImpl userDetails, Integer profileId, EmploymentHistoryCreateDto dto) {
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new ProfileNotFoundException(profileId, this));

        AuthorityUtil.validateAuthority(profile.getManager(), userDetails, 4);

        EmploymentHistory employmentHistory = EmploymentHistory.builder()
                .type(dto.getType())
                .step(dto.getStep())
                .result(dto.getResult())
                .targetDepartmentName(dto.getTargetDepartmentName())
                .targetJobRankName(dto.getTargetJobRankName())
                .executiveName(dto.getExecutiveName())
                .targetEmployDate(dto.getTargetEmployDate())
                .description(dto.getDescription())
                .profile(profile)
                .member(memberRepository.findById(userDetails.getMemberId()).orElseThrow(() -> new MemberNotFoundException(userDetails.getMemberId(), this)))
                .build();

        employmentHistoryRepository.save(employmentHistory);

        return employmentHistory;
    }

    @Override
    public List<EmploymentHistory> readEmploymentHistories(UserDetailsImpl userDetails, Integer profileId) {
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new ProfileNotFoundException(profileId, this));

        AuthorityUtil.validateAuthority(profile.getManager(), userDetails, 4);

        return employmentHistoryRepository.findAllByProfileId(profileId);
    }

    @Override
    public EmploymentHistory update(UserDetailsImpl userDetails, Long employmentHistoryId, EmploymentHistoryCreateDto dto) {
        EmploymentHistory employmentHistory = employmentHistoryRepository.findById(employmentHistoryId)
                .orElseThrow(() -> new EmploymentHistoryNotFoundException(employmentHistoryId, this));

        AuthorityUtil.validateAuthority(employmentHistory.getProfile().getManager(), userDetails, 4);

        employmentHistory.update(
                dto.getResult(),
                dto.getTargetDepartmentName(),
                dto.getTargetJobRankName(),
                dto.getExecutiveName(),
                dto.getTargetEmployDate(),
                dto.getDescription()
        );

        return employmentHistory;
    }

    @Override
    public EmploymentHistory updateFavorite(UserDetailsImpl userDetails, Long employmentHistoryId, Boolean isFavorite) {
        EmploymentHistory employmentHistory = employmentHistoryRepository.findById(employmentHistoryId)
                .orElseThrow(() -> new EmploymentHistoryNotFoundException(employmentHistoryId, this));

        AuthorityUtil.validateAuthority(employmentHistory.getProfile().getManager(), userDetails, 4);

        employmentHistory.updateFavorite(isFavorite);

        return employmentHistory;
    }

    @Override
    public void delete(UserDetailsImpl userDetails, Long employmentHistoryId) {
        EmploymentHistory employmentHistory = employmentHistoryRepository.findById(employmentHistoryId)
                        .orElseThrow(() -> new EmploymentHistoryNotFoundException(employmentHistoryId, this));

        AuthorityUtil.validateAuthority(employmentHistory.getProfile().getManager(), userDetails, 4);

        employmentHistoryRepository.delete(employmentHistory);
    }
}

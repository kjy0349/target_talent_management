package com.ssafy.s10p31s102be.profile.service;

import com.ssafy.s10p31s102be.common.util.AuthorityUtil;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.member.exception.DepartmentNotFoundException;
import com.ssafy.s10p31s102be.member.exception.JobRankNotFoundException;
import com.ssafy.s10p31s102be.member.exception.MemberNotFoundException;
import com.ssafy.s10p31s102be.member.infra.entity.Department;
import com.ssafy.s10p31s102be.member.infra.entity.JobRank;
import com.ssafy.s10p31s102be.member.infra.repository.DepartmentJpaRepository;
import com.ssafy.s10p31s102be.member.infra.repository.JobRankJpaRepository;
import com.ssafy.s10p31s102be.member.infra.repository.MemberJpaRepository;
import com.ssafy.s10p31s102be.profile.dto.request.UsagePlanCreateDto;
import com.ssafy.s10p31s102be.profile.exception.ProfileNotFoundException;
import com.ssafy.s10p31s102be.profile.exception.UsagePlanNotFoundException;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.entity.community.UsagePlan;
import com.ssafy.s10p31s102be.profile.infra.repository.ProfileJpaRepository;
import com.ssafy.s10p31s102be.profile.infra.repository.UsagePlanJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UsagePlanServiceImpl implements UsagePlanService {

    private final ProfileJpaRepository profileRepository;
    private final UsagePlanJpaRepository usagePlanRepository;
    private final MemberJpaRepository memberRepository;

    @Override
    public UsagePlan create(UserDetailsImpl userDetails, Integer profileId, UsagePlanCreateDto dto) {
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new ProfileNotFoundException(profileId, this));

        AuthorityUtil.validateAuthority(profile.getManager(), userDetails, 5);

        UsagePlan usagePlan = UsagePlan.builder()
                .member(memberRepository.findById(userDetails.getMemberId()).orElseThrow(() -> new MemberNotFoundException(userDetails.getMemberId(), this)))
                .mainDescription(dto.getMainDescription())
                .detailDescription(dto.getDetailDescription())
                .targetEmployDate(dto.getTargetEmployDate())
                .jobRank(dto.getJobRank())
                .targetDepartmentName(dto.getTargetDepartmentName())
                .profile(profile)
                .build();

        return usagePlanRepository.save(usagePlan);
    }

    @Override
    public List<UsagePlan> readUsagePlans(UserDetailsImpl userDetails, Integer profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException(profileId, this));

        AuthorityUtil.validateAuthority(profile.getManager(), userDetails, 5);

        return usagePlanRepository.findAllByProfileId(profileId);
    }

    @Override
    public UsagePlan update(UserDetailsImpl userDetails, Integer usagePlanId, UsagePlanCreateDto dto) {
        UsagePlan usagePlan = usagePlanRepository.findById(usagePlanId).orElseThrow(() -> new UsagePlanNotFoundException(usagePlanId, this));

        AuthorityUtil.validateAuthority(usagePlan.getProfile().getManager(), userDetails, 5);

        usagePlan.update(
                dto.getMainDescription(),
                dto.getDetailDescription(),
                dto.getTargetEmployDate(),
                dto.getJobRank(),
                dto.getTargetDepartmentName()
        );

        return usagePlan;
    }

    @Override
    public UsagePlan updateFavorite(UserDetailsImpl userDetails, Integer usagePlanId, Boolean isFavorite) {
        UsagePlan usagePlan = usagePlanRepository.findById(usagePlanId).orElseThrow(() -> new UsagePlanNotFoundException(usagePlanId, this));

        AuthorityUtil.validateAuthority(usagePlan.getProfile().getManager(), userDetails, 5);

        usagePlan.updateFavorite(isFavorite);

        return usagePlan;
    }

    @Override
    public void delete(UserDetailsImpl userDetails, Integer usagePlanId) {
        UsagePlan usagePlan = usagePlanRepository.findById(usagePlanId).orElseThrow(() -> new UsagePlanNotFoundException(usagePlanId, this));

        AuthorityUtil.validateAuthority(usagePlan.getProfile().getManager(), userDetails, 4);

        usagePlanRepository.delete(usagePlan);
    }
}

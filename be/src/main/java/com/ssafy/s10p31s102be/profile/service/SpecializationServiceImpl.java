package com.ssafy.s10p31s102be.profile.service;

import com.ssafy.s10p31s102be.common.exception.InvalidAuthorizationException;
import com.ssafy.s10p31s102be.common.util.AuthorityUtil;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.member.exception.MemberNotFoundException;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.member.infra.repository.MemberJpaRepository;
import com.ssafy.s10p31s102be.profile.dto.request.SpecializationCreateDto;
import com.ssafy.s10p31s102be.profile.exception.ProfileNotFoundException;
import com.ssafy.s10p31s102be.profile.exception.SpecializationNotFoundException;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.entity.career.Specialization;
import com.ssafy.s10p31s102be.profile.infra.repository.ProfileJpaRepository;
import com.ssafy.s10p31s102be.profile.infra.repository.SpecializationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SpecializationServiceImpl implements SpecializationService {
    private final SpecializationJpaRepository specializationRepository;
    private final ProfileJpaRepository profileRepository;
    private final MemberJpaRepository memberRepository;

    @Override
    public Specialization create(UserDetailsImpl userDetails, Integer profileId, SpecializationCreateDto dto) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException(profileId, this));

        AuthorityUtil.validateAuthority(profile.getManager(), userDetails, 5);

        Specialization specialization = Specialization.builder()
                .member(memberRepository.findById(userDetails.getMemberId()).orElseThrow(() -> new MemberNotFoundException(userDetails.getMemberId(), this)))
                .specialPoint(dto.getSpecialPoint())
                .description(dto.getDescription())
                .profile(profile)
                .build();

        return specializationRepository.save(specialization);
    }

    @Override
    public List<Specialization> readSpecializations(UserDetailsImpl userDetails, Integer profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException(profileId, this));

        AuthorityUtil.validateAuthority(profile.getManager(), userDetails, 5);

        return specializationRepository.findAllByProfileId(profileId);
    }

    @Override
    public Specialization update(UserDetailsImpl userDetails, Long specializationId, SpecializationCreateDto dto) {
        Specialization specialization = specializationRepository.findById(specializationId)
                .orElseThrow(() -> new SpecializationNotFoundException(specializationId, this));

        AuthorityUtil.validateAuthority(specialization.getProfile().getManager(), userDetails, 5);

        specialization.update(
                dto.getSpecialPoint(),
                dto.getDescription(),
                memberRepository.findById(userDetails.getMemberId()).orElseThrow(() -> new MemberNotFoundException(userDetails.getMemberId(), this))
        );

        return specialization;
    }

    @Override
    public Specialization updateFavorite(UserDetailsImpl userDetails, Long specializationId, Boolean isFavorite) {
        Specialization specialization = specializationRepository.findById(specializationId)
                .orElseThrow(() -> new SpecializationNotFoundException(specializationId, this));

        AuthorityUtil.validateAuthority(specialization.getProfile().getManager(), userDetails, 5);

        specialization.updateFavorite(isFavorite);

        return specialization;
    }
    @Override
    public void delete(UserDetailsImpl userDetails, Long specializationId) {
        Specialization specialization = specializationRepository.findById(specializationId)
                .orElseThrow(() -> new SpecializationNotFoundException(specializationId, this));

        AuthorityUtil.validateAuthority(specialization.getProfile().getManager(), userDetails, 4);

        specializationRepository.delete(specialization);
    }
}

package com.ssafy.s10p31s102be.profile.service;

import com.ssafy.s10p31s102be.common.exception.InvalidAuthorizationException;
import com.ssafy.s10p31s102be.common.util.AuthorityUtil;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.member.exception.MemberNotFoundException;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.member.infra.repository.MemberJpaRepository;
import com.ssafy.s10p31s102be.profile.dto.request.ProfileMemoCreateDto;
import com.ssafy.s10p31s102be.profile.exception.ProfileMemoNotFoundException;
import com.ssafy.s10p31s102be.profile.exception.ProfileNotFoundException;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.entity.ProfileMemo;
import com.ssafy.s10p31s102be.profile.infra.repository.ProfileJpaRepository;
import com.ssafy.s10p31s102be.profile.infra.repository.ProfileMemoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileMemoServiceImpl implements ProfileMemoService {

    private final ProfileJpaRepository profileRepository;
    private final MemberJpaRepository memberRepository;
    private final ProfileMemoJpaRepository profileMemoRepository;

    @Override
    public ProfileMemo create(UserDetailsImpl userDetails, Integer profileId, ProfileMemoCreateDto dto) {
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new ProfileNotFoundException(profileId, this));
        Member member = memberRepository.findById(userDetails.getMemberId()).orElseThrow(() -> new MemberNotFoundException(userDetails.getMemberId(), this));

        AuthorityUtil.validateAuthority(profile.getManager(), userDetails, 4);

        ProfileMemo profileMemo = ProfileMemo.builder()
                .profile(profile)
                .member(member)
                .content(dto.getContent())
                .isPrivate(dto.getIsPrivate())
                .build();

        return profileMemoRepository.save(profileMemo);
    }

    @Override
    public ProfileMemo update(UserDetailsImpl userDetails, Long profileMemoId, ProfileMemoCreateDto dto) {
        ProfileMemo profileMemo = profileMemoRepository.findById(profileMemoId).orElseThrow(() -> new ProfileMemoNotFoundException(profileMemoId, this));
        Member member = memberRepository.findById(userDetails.getMemberId()).orElseThrow(() -> new MemberNotFoundException(userDetails.getMemberId(), this));

        if (userDetails.getAuthorityLevel() > 2 && !(userDetails.getMemberId().equals(profileMemo.getMember().getId()))) {
            throw new InvalidAuthorizationException(userDetails.getMemberId(), this);
        }

        profileMemo.update(
                member,
                dto.getContent(),
                dto.getIsPrivate()
        );

        return profileMemo;
    }

    @Override
    public List<ProfileMemo> readProfileMemos(UserDetailsImpl userDetails, Integer profileId) {
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new ProfileNotFoundException(profileId, this));

        AuthorityUtil.validateAuthority(profile.getManager(), userDetails, 4);

        return profile.getProfileMemos().stream().filter(profileMemo -> {
            return userDetails.getAuthorityLevel() <= 2 || !profileMemo.getIsPrivate() || profileMemo.getMember().getId().equals(userDetails.getMemberId());
        }).toList();
    }

    @Override
    public void delete(UserDetailsImpl userDetails, Long profileMemoId) {
        ProfileMemo profileMemo = profileMemoRepository.findById(profileMemoId).orElseThrow(() -> new ProfileMemoNotFoundException(profileMemoId, this));

        if (userDetails.getAuthorityLevel() > 2 && !(userDetails.getMemberId().equals(profileMemo.getMember().getId()))) {
            throw new InvalidAuthorizationException(userDetails.getMemberId(), this);
        }

        profileMemoRepository.delete(profileMemo);
    }
}

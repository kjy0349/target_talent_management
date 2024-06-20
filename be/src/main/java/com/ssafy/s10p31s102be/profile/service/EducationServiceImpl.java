package com.ssafy.s10p31s102be.profile.service;

import com.ssafy.s10p31s102be.common.infra.entity.Keyword;
import com.ssafy.s10p31s102be.common.infra.enums.KeywordType;
import com.ssafy.s10p31s102be.common.infra.repository.KeywordJpaRepository;
import com.ssafy.s10p31s102be.common.util.AuthorityUtil;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.profile.dto.request.EducationCreateDto;
import com.ssafy.s10p31s102be.profile.exception.EducationNotFoundException;
import com.ssafy.s10p31s102be.profile.exception.LabNotFoundException;
import com.ssafy.s10p31s102be.profile.exception.ProfileNotFoundException;
import com.ssafy.s10p31s102be.profile.exception.SchoolNotFoundException;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.entity.education.Education;
import com.ssafy.s10p31s102be.profile.infra.entity.education.KeywordEducation;
import com.ssafy.s10p31s102be.profile.infra.entity.education.Lab;
import com.ssafy.s10p31s102be.profile.infra.entity.education.School;
import com.ssafy.s10p31s102be.profile.infra.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EducationServiceImpl implements EducationService {
    private final ProfileJpaRepository profileRepository;
    private final EducationJpaRepository educationRepository;
    private final KeywordJpaRepository keywordRepository;
    private final SchoolJpaRepository schoolRepository;
    private final LabJpaRepository labRepository;
    private final KeywordEducationJpaRepository keywordEducationRepository;

    private final KeywordType KEYWORD_IDENTIFIER = KeywordType.TECH_SKILL;

    @Override
    public Education create(UserDetailsImpl userDetails, Integer profileId, EducationCreateDto dto) {
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new ProfileNotFoundException(profileId, this));
        // 권한 체크
        // Assistant 5번부터 수정 가능
        AuthorityUtil.validateAuthority(profile.getManager(), userDetails, 5);

        School school = findOrCreateSchool(dto);
        Lab lab = findOrCreateLab(dto, school);

        Education education = Education.builder()
                .degree(dto.getDegree())
                .major(dto.getMajor())
                .enteredAt(dto.getEnteredAt())
                .graduatedAt(dto.getGraduatedAt())
                .graduateStatus(dto.getGraduateStatus())
                .school(school)
                .lab(lab)
                .labResearchType(dto.getLabResearchType())
                .labResearchResult(dto.getLabResearchResult())
                .labResearchDescription(dto.getLabResearchDescription())
                .profile(profile)
                .build();

        educationRepository.save(education);
        addEducationKeywords(education, dto);

        educationRepository.save(education);
        return education;
    }

    @Override
    public Education read(Long educationId) {
        return educationRepository.findById(educationId).orElseThrow(() -> new EducationNotFoundException(educationId, this));
    }

    @Override
    public Education update(UserDetailsImpl userDetails, Long educationId, EducationCreateDto dto) {
        Education education = educationRepository.findById(educationId).orElseThrow(() -> new EducationNotFoundException(educationId, this));

        School school = findOrCreateSchool(dto);
        Lab lab = findOrCreateLab(dto, school);
        // 권한 체크
        int profileId = education.getProfile().getId();
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new ProfileNotFoundException(profileId, this));
        // Assistant 5번부터 수정 가능
        AuthorityUtil.validateAuthority(profile.getManager(), userDetails, 5);

        education.update(
                dto.getDegree(),
                dto.getMajor(),
                dto.getEnteredAt(),
                dto.getGraduatedAt(),
                dto.getGraduateStatus(),
                school,
                lab,
                dto.getLabResearchType(),
                dto.getLabResearchResult(),
                dto.getLabResearchDescription()
        );

        keywordEducationRepository.deleteAll(education.getKeywordEducation());
        education.getKeywordEducation().clear();

        educationRepository.save(education);

        addEducationKeywords(education, dto);

        educationRepository.save(education);
        return education;
    }

    @Override
    public void delete(UserDetailsImpl userDetails, Long educationId) {
        Education education = educationRepository.findById(educationId).orElseThrow(() -> new EducationNotFoundException(educationId, this));

        // 권한 체크
        int profileId = education.getProfile().getId();
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new ProfileNotFoundException(profileId, this));
        // Assistant 5번부터 수정 가능
        AuthorityUtil.validateAuthority(profile.getManager(), userDetails, 5);

        educationRepository.delete(education);
    }

    private void addEducationKeywords(Education education, EducationCreateDto dto) {
        if (dto.getKeywords() == null) return;

        education.getKeywordEducation().addAll(
                dto.getKeywords().stream()
                        .map(key -> {
                            Keyword keyword = keywordRepository.findByTypeAndData(KEYWORD_IDENTIFIER, key)
                                    .orElseGet(() -> new Keyword(KEYWORD_IDENTIFIER, key));
                            keyword.use();

                            KeywordEducation build = KeywordEducation.builder()
                                    .education(education)
                                    .keyword(keyword)
                                    .build();
                            keywordEducationRepository.save(build);
                            return build;
                        }).toList()
        );
    }

    private School findOrCreateSchool(EducationCreateDto dto) {
        return schoolRepository.findBySchoolName(dto.getSchoolName()).orElseGet(() ->
                School.builder()
                        .schoolName(dto.getSchoolName())
                        .country(dto.getSchoolCountry())
                        .build()
        );
    }

    private Lab findOrCreateLab(EducationCreateDto dto, School school) {
        if (dto.getLabName() == null || dto.getLabName().isEmpty()) return null;

        return labRepository.findByLabName(dto.getLabName()).orElseGet(() ->
                Lab.builder()
                        .labName(dto.getLabName())
                        .labProfessor(dto.getLabProfessor())
                        .researchDescription(dto.getLabResearchDescription())
                        .researchType(dto.getLabResearchType())
                        .researchResult(dto.getLabResearchResult())
                        .school(school)
                        .build()
        );
    }
}

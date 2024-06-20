package com.ssafy.s10p31s102be.profile.service;

import com.ssafy.s10p31s102be.common.infra.enums.KeywordType;
import com.ssafy.s10p31s102be.common.infra.repository.KeywordJpaRepository;
import com.ssafy.s10p31s102be.common.util.AuthorityUtil;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.profile.dto.request.CareerCreateDto;
import com.ssafy.s10p31s102be.profile.exception.CareerNotFoundException;
import com.ssafy.s10p31s102be.profile.exception.CompanyNotFoundException;
import com.ssafy.s10p31s102be.profile.exception.ProfileNotFoundException;
import com.ssafy.s10p31s102be.common.infra.entity.Keyword;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.entity.career.Career;
import com.ssafy.s10p31s102be.profile.infra.entity.career.CareerKeyword;
import com.ssafy.s10p31s102be.profile.infra.entity.career.Company;
import com.ssafy.s10p31s102be.profile.infra.repository.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CareerServiceImpl implements CareerService {

    private final ProfileJpaRepository profileRepository;
    private final CareerJpaRepository careerRepository;
    private final KeywordJpaRepository keywordRepository;
    private final CompanyJpaRepository companyRepository;
    private final CountryJpaRepository countryRepository;
    private final CareerKeywordJpaRepository careerKeywordRepository;

    private final KeywordType KEYWORD_IDENTIFIER = KeywordType.TECH_SKILL;

    @Override
    public Career create(UserDetailsImpl userDetails, Integer profileId, CareerCreateDto dto) {
        /*
            나만보기 체크는 제외한다. 나만보기 체크를 추가할 시, Excel Import를 수행할 때 같은 부서이지만 경력 정보 등록 불가능.
            추후에 다른 createMethod로 빼서 리팩토링.
        */
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new ProfileNotFoundException(profileId, this));
        // Assistant 5번부터 생성 가능
        AuthorityUtil.validateAuthority(profile.getManager(), userDetails, 5);

        Company company = companyRepository.findByName(dto.getCompanyName()).orElseGet(() -> Company.builder()
                .name(dto.getCompanyName())
                .build()
        );

        LocalDateTime startedAt = dto.getStartedAt();
        LocalDateTime endedAt = dto.getEndedAt();

        Long careerPeriodMonth = null;
        if (startedAt != null) {
            endedAt = (endedAt != null) ? endedAt : LocalDateTime.now();
            careerPeriodMonth = (long) ChronoUnit.MONTHS.between(startedAt, endedAt);
        }

        Career career = Career.builder()
                .jobRank(dto.getJobRank())
                .level(dto.getLevel())
                .isManager(dto.getIsManager())
                .employType(dto.getEmployType())
                .startedAt(dto.getStartedAt())
                .endedAt(dto.getEndedAt())
                .careerPeriodMonth(careerPeriodMonth)
                .isCurrent(dto.getIsCurrent())
                .dept(dto.getDept())
                .role(dto.getRole())
                .description(dto.getDescription())
                .profile(profile)
                .company(company)
                .country(dto.getCompanyCountryName())
                .region(dto.getCompanyRegion())
                .build();

        careerRepository.save(career);
        addCareerKeywords(career, dto);

        careerRepository.save(career);
        return career;
    }

    @Override
    public Career read(Long careerId) {
        return careerRepository.findById(careerId).orElseThrow(() -> new CareerNotFoundException(careerId, this));
    }

    @Override
    public Career update(UserDetailsImpl userDetails, Long careerId, CareerCreateDto dto) {
        Career career = careerRepository.findById(careerId).orElseThrow(() -> new CareerNotFoundException(careerId, this));

        // 권한 체크
        int profileId = career.getProfile().getId();
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new ProfileNotFoundException(profileId, this));
        // Assistant 5번부터 수정 가능
        AuthorityUtil.validateAuthority(profile.getManager(), userDetails, 5);

        LocalDateTime startedAt = dto.getStartedAt();
        LocalDateTime endedAt = dto.getEndedAt();

        Long careerPeriodMonth = null;
        if (startedAt != null) {
            endedAt = (endedAt != null) ? endedAt : LocalDateTime.now();
            careerPeriodMonth = (long) ChronoUnit.MONTHS.between(startedAt, endedAt);
        }

        career.update(
                dto.getJobRank(),
                dto.getStartedAt(),
                dto.getEndedAt(),
                careerPeriodMonth,
                dto.getEmployType(),
                dto.getLevel(),
                dto.getIsManager(),
                dto.getIsCurrent(),
                dto.getDept(),
                dto.getRole(),
                dto.getDescription(),
                companyRepository.findByName(dto.getCompanyName()).orElseGet(
                        () -> Company.builder()
                                .name(dto.getCompanyName())
                                .build()
                ),
                dto.getCompanyCountryName(),
                dto.getCompanyRegion()
        );

        careerKeywordRepository.deleteAll(career.getCareerKeywords());
        career.getCareerKeywords().clear();

        addCareerKeywords(career, dto);

        careerRepository.save(career);
        return career;
    }

    @Override
    public void delete(UserDetailsImpl userDetails, Long careerId) {
        Career career = careerRepository.findById(careerId).orElseThrow(() -> new CareerNotFoundException(careerId, this));
        // 권한 체크
        int profileId = career.getProfile().getId();
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new ProfileNotFoundException(profileId, this));
        // Assistant 5번부터 수정 가능
        AuthorityUtil.validateAuthority(profile.getManager(), userDetails, 5);

        careerRepository.delete(career);
    }

    private void addCareerKeywords(Career career, CareerCreateDto dto) {
        if (dto.getKeywords() == null) return;

        career.getCareerKeywords().addAll(
                dto.getKeywords().stream()
                        .map(key -> {
                            Keyword keyword = keywordRepository.findByTypeAndData(KEYWORD_IDENTIFIER, key)
                                    .orElseGet(() -> new Keyword(KEYWORD_IDENTIFIER, key));
                            keyword.use();

                            CareerKeyword build = CareerKeyword.builder()
                                    .career(career)
                                    .keyword(keyword)
                                    .build();
                            careerKeywordRepository.save(build);
                            return build;
                        }).toList()
        );
    }
}

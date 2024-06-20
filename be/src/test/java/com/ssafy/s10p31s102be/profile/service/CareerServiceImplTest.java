package com.ssafy.s10p31s102be.profile.service;

import com.ssafy.s10p31s102be.common.infra.enums.KeywordType;
import com.ssafy.s10p31s102be.common.infra.repository.KeywordJpaRepository;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.profile.dto.request.CareerCreateDto;
import com.ssafy.s10p31s102be.profile.infra.entity.Country;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.entity.career.Career;
import com.ssafy.s10p31s102be.profile.infra.entity.career.Company;
import com.ssafy.s10p31s102be.profile.infra.enums.EmployType;
import com.ssafy.s10p31s102be.profile.infra.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CareerServiceImplTest {

    @Mock
    private ProfileJpaRepository profileRepository;

    @Mock
    private CareerJpaRepository careerRepository;

    @Mock
    private CountryJpaRepository countryRepository;

    @Mock
    private CompanyJpaRepository companyRepository;

    @Mock
    private KeywordJpaRepository keywordRepository;

    @Mock
    private CareerKeywordJpaRepository careerKeywordRepository;

    @InjectMocks
    private CareerServiceImpl careerService;

    private Country mockCountry;
    private Company mockCompany;
    private Profile mockProfile;
    private Career mockCareer;
    private CareerCreateDto dto;

    @BeforeEach
    void 경력_테스트_초기_설정() {
        Member mockMember = new Member();

        mockProfile = Profile.builder()
                .manager(mockMember)
                .build();

        List<String> mockKeywords = new ArrayList<>();
        mockKeywords.add("Spring Boot");
        mockKeywords.add("Backend");

        dto = CareerCreateDto.builder()
                .companyName("삼성전자")
                .companyCountryName("한국")
                .companyRegion("수원시")
                .jobRank("C2")
                .isManager(false)
                .employType(EmployType.FULL_TIME)
                .startedAt(LocalDateTime.now())
                .endedAt(LocalDateTime.now())
                .isCurrent(true)
                .dept("DA")
                .role("Service Engineer")
                .keywords(mockKeywords)
                .build();

        mockCountry = Country.builder()
                .name("한국")
                .code("KR")
                .continent("Asia")
                .build();

        mockCompany = Company.builder()
                .name("삼성전자")
                .country(mockCountry)
                .region("수원시")
                .build();

        mockCareer = Career.builder()
                .jobRank("C2")
                .isManager(false)
                .employType(EmployType.FULL_TIME)
                .startedAt(LocalDateTime.now())
                .endedAt(LocalDateTime.now())
                .isCurrent(true)
                .dept("DA")
                .role("Service Engineer")
                .profile(mockProfile)
                .company(mockCompany)
                .build();
    }

    @Test
    void 경력_정보를_생성할_수_있다() {
        when(profileRepository.findById(1)).thenReturn(Optional.of(mockProfile));
        when(countryRepository.findByName("한국")).thenReturn(Optional.of(mockCountry));
        when(keywordRepository.findByTypeAndData(eq(KeywordType.TECH_SKILL), any())).thenReturn(Optional.empty());

        Career createdCareer = careerService.create(1, dto);

        assertNotNull(createdCareer);
        assertEquals(dto.getJobRank(), createdCareer.getJobRank());
        assertEquals(dto.getIsManager(), createdCareer.getIsManager());
        assertEquals(dto.getEmployType(), createdCareer.getEmployType());
        assertEquals(dto.getStartedAt(), createdCareer.getStartedAt());
        assertEquals(dto.getEndedAt(), createdCareer.getEndedAt());
        assertEquals(dto.getIsCurrent(), createdCareer.getIsCurrent());
        assertEquals(dto.getDept(), createdCareer.getDept());
        assertEquals(dto.getRole(), createdCareer.getRole());
        assertEquals(mockProfile, createdCareer.getProfile());
        assertEquals(dto.getCompanyName(), createdCareer.getCompany().getName());
        assertEquals(dto.getCompanyCountryName(), createdCareer.getCompany().getCountry().getName());
        assertEquals(dto.getCompanyRegion(), createdCareer.getCompany().getRegion());
        assertEquals(2, createdCareer.getCareerKeywords().size());
    }

    @Test
    void 경력_정보를_조회할_수_있다() {
        when(careerRepository.findById(1L)).thenReturn(Optional.of(mockCareer));

        Career career = careerService.read(1L);

        assertNotNull(career);
        assertEquals(mockCareer.getJobRank(), career.getJobRank());
        assertEquals(mockCareer.getIsManager(), career.getIsManager());
        assertEquals(mockCareer.getEmployType(), career.getEmployType());
        assertEquals(mockCareer.getStartedAt(), career.getStartedAt());
        assertEquals(mockCareer.getEndedAt(), career.getEndedAt());
        assertEquals(mockCareer.getIsCurrent(), career.getIsCurrent());
        assertEquals(mockCareer.getDept(), career.getDept());
        assertEquals(mockCareer.getRole(), career.getRole());
        assertEquals(mockProfile, career.getProfile());
        assertEquals(mockCompany, career.getCompany());
    }

    @Test
    void 경력_정보를_수정할_수_있다() {
        when(careerRepository.findById(1L)).thenReturn(Optional.of(mockCareer));
        when(countryRepository.findByName("한국")).thenReturn(Optional.of(mockCountry));
        when(keywordRepository.findByTypeAndData(eq(KeywordType.TECH_SKILL), any())).thenReturn(Optional.empty());

        Career updatedCareer = careerService.update(1L, dto);

        assertNotNull(updatedCareer);
        assertEquals(dto.getJobRank(), updatedCareer.getJobRank());
        assertEquals(dto.getIsManager(), updatedCareer.getIsManager());
        assertEquals(dto.getEmployType(), updatedCareer.getEmployType());
        assertEquals(dto.getStartedAt(), updatedCareer.getStartedAt());
        assertEquals(dto.getEndedAt(), updatedCareer.getEndedAt());
        assertEquals(dto.getIsCurrent(), updatedCareer.getIsCurrent());
        assertEquals(dto.getDept(), updatedCareer.getDept());
        assertEquals(dto.getRole(), updatedCareer.getRole());
        assertEquals(mockProfile, updatedCareer.getProfile());
        assertEquals(dto.getCompanyName(), updatedCareer.getCompany().getName());
        assertEquals(dto.getCompanyCountryName(), updatedCareer.getCompany().getCountry().getName());
        assertEquals(dto.getCompanyRegion(), updatedCareer.getCompany().getRegion());
        assertEquals(2, updatedCareer.getCareerKeywords().size());
    }

    @Test
    void 경력_정보를_삭제할_수_있다() {
        when(careerRepository.findById(1L)).thenReturn(Optional.of(mockCareer));

        careerService.delete(1L);
        verify(careerRepository, times(1)).delete(mockCareer);
    }

}
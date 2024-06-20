package com.ssafy.s10p31s102be.profile.service;

import com.ssafy.s10p31s102be.common.infra.repository.KeywordJpaRepository;
import com.ssafy.s10p31s102be.member.infra.entity.Authority;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.profile.dto.request.EducationCreateDto;
import com.ssafy.s10p31s102be.profile.exception.EducationNotFoundException;
import com.ssafy.s10p31s102be.profile.exception.ProfileNotFoundException;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.entity.education.Education;
import com.ssafy.s10p31s102be.profile.infra.entity.education.Lab;
import com.ssafy.s10p31s102be.profile.infra.entity.education.School;
import com.ssafy.s10p31s102be.profile.infra.enums.Degree;
import com.ssafy.s10p31s102be.profile.infra.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EducationServiceTest {

    @Mock
    private ProfileJpaRepository profileRepository;
    @Mock
    private EducationJpaRepository educationRepository;
    @Mock
    private KeywordJpaRepository keywordRepository;
    @Mock
    private SchoolJpaRepository schoolRepository;
    @Mock
    private LabJpaRepository labRepository;
    @Mock
    private KeywordEducationJpaRepository keywordEducationRepository;

    @InjectMocks
    private EducationServiceImpl educationService;

    private Profile mockProfile;
    private Education mockEducation;
    private EducationCreateDto dto;

    @BeforeEach
    void 학력_테스트_초기_설정() {

        Member mockMember = new Member();

        mockProfile = Profile.builder()
                .manager(mockMember)
                .build();

        List<String> mockKeywords = new ArrayList<>();
        mockKeywords.add("Spring Boot");
        mockKeywords.add("Backend");

        dto = EducationCreateDto.builder()
                .degree(Degree.MASTER)
                .schoolCountry("한국")
                .schoolName("테스트 대학교")
                .major("컴퓨터공학")
                .enteredAt(LocalDateTime.now())
                .graduatedAt(LocalDateTime.now())
                .labName("테스트 연구실")
                .labResearchType("Backend")
                .labResearchDescription("Spring Boot")
                .labResearchResult("구현 성공")
                .labProfessor("테스트 교수")
                .keywords(mockKeywords)
                .build();

        mockEducation = Education.builder()
                .degree(Degree.BACHELOR)
                .major("경영학과")
                .school(
                        School.builder()
                                .schoolName("기존 대학교")
                                .country("미국")
                                .build()
                )
                .profile(mockProfile)
                .build();
    }

    @Test
    void 학력_정보를_생성할_수_있다() throws Exception {
        when(profileRepository.findById(1)).thenReturn(Optional.of(mockProfile));
        when(schoolRepository.findBySchoolName(dto.getSchoolName())).thenReturn(Optional.empty());
        when(labRepository.findByLabName(dto.getLabName())).thenReturn(Optional.empty());

        when(educationRepository.save(any(Education.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Education createdEducation = educationService.create(1, dto);

        assertNotNull(createdEducation);
        assertEquals(dto.getDegree(), createdEducation.getDegree());
        assertEquals(dto.getSchoolName(), createdEducation.getSchool().getSchoolName());
        assertEquals(dto.getSchoolCountry(), createdEducation.getSchool().getCountry());
        assertEquals(dto.getMajor(), createdEducation.getMajor());
        assertEquals(dto.getEnteredAt(), createdEducation.getEnteredAt());
        assertEquals(dto.getGraduatedAt(), createdEducation.getGraduatedAt());
        assertEquals(dto.getLabName(), createdEducation.getLab().getLabName());
        assertEquals(dto.getLabResearchType(), createdEducation.getLab().getResearchType());
        assertEquals(dto.getLabResearchDescription(), createdEducation.getLab().getResearchDescription());
        assertEquals(dto.getLabResearchResult(), createdEducation.getLab().getResearchResult());
        assertEquals(dto.getLabProfessor(), createdEducation.getLab().getLabProfessor());
        assertEquals(mockProfile, createdEducation.getProfile());
        assertEquals(dto.getKeywords().size(), createdEducation.getKeywordEducation().size());
    }

    @Test
    void 학력_정보를_수정할_수_있다() throws Exception {

        when(educationRepository.findById(1L)).thenReturn(Optional.of(mockEducation));
        when(schoolRepository.findBySchoolName(dto.getSchoolName())).thenReturn(Optional.empty());
        when(labRepository.findByLabName(dto.getLabName())).thenReturn(Optional.empty());

        Education updatedEducation = educationService.update(1L, dto);

        assertNotNull(updatedEducation);
        assertEquals(dto.getDegree(), updatedEducation.getDegree());
        assertEquals(dto.getSchoolName(), updatedEducation.getSchool().getSchoolName());
        assertEquals(dto.getSchoolCountry(), updatedEducation.getSchool().getCountry());
        assertEquals(dto.getMajor(), updatedEducation.getMajor());
        assertEquals(dto.getEnteredAt(), updatedEducation.getEnteredAt());
        assertEquals(dto.getGraduatedAt(), updatedEducation.getGraduatedAt());
        assertEquals(dto.getLabName(), updatedEducation.getLab().getLabName());
        assertEquals(dto.getLabResearchType(), updatedEducation.getLab().getResearchType());
        assertEquals(dto.getLabResearchDescription(), updatedEducation.getLab().getResearchDescription());
        assertEquals(dto.getLabResearchResult(), updatedEducation.getLab().getResearchResult());
        assertEquals(dto.getLabProfessor(), updatedEducation.getLab().getLabProfessor());
        assertEquals(mockProfile, updatedEducation.getProfile());
        assertEquals(dto.getKeywords().size(), updatedEducation.getKeywordEducation().size());
    }

    @Test
    void 학력_정보를_삭제할_수_있다() throws Exception {
        when(educationRepository.findById(1L)).thenReturn(Optional.of(mockEducation));

        educationService.delete(1L);
        verify(educationRepository, times(1)).delete(mockEducation);
    }
}
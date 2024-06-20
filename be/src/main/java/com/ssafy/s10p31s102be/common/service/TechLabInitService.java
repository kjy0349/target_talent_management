package com.ssafy.s10p31s102be.common.service;

import com.ssafy.s10p31s102be.profile.infra.entity.education.Lab;
import com.ssafy.s10p31s102be.profile.infra.entity.education.School;
import com.ssafy.s10p31s102be.profile.infra.repository.LabJpaRepository;
import com.ssafy.s10p31s102be.profile.infra.repository.SchoolJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TechLabInitService {
    private final LabJpaRepository labRepository;

    private final SchoolJpaRepository schoolRepository;

//    @PostConstruct
//    @Transactional
    public void insertMember(){

        // 학력 생성
        School school1 = new School("테스트 학교 1", "한국");
        School school2 = new School("테스트학교 2", "미국");

        //연구소
        Lab lab = Lab.builder()
                .labName("싸피")
                .labProfessor("조용준")
                .researchDescription("test")
                .researchResult("GOOD")
                .researchType("AI")
                .school(school1)
                .build();
        labRepository.save(lab);

        Lab lab2 = Lab.builder()
                .labName("싸피멀캠")
                .labProfessor("김태희")
                .researchDescription("test")
                .researchResult("GOOD")
                .researchType("AI")
                .school(school2)
                .build();
        labRepository.save(lab2);
    }
}

package com.ssafy.s10p31s102be.dashboard.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class DashboardUtils {

    public DashboardUtils() {
        this.jobMapper = new HashMap<>();


        for (String jobTitle : developerJobTitles) {
            jobMapper.put(jobTitle, true);
        }
    }

    private final Map<String,Boolean> jobMapper;
    private final String[] developerJobTitles = {
            "Software Developer", "Software Engineer", "Full Stack Developer", "Backend Developer",
            "Frontend Developer", "Mobile Developer", "Web Developer", "Application Developer",
            "Java Developer", "Python Developer", "JavaScript Developer", ".NET Developer",
            "Ruby on Rails Developer", "PHP Developer", "C++ Developer", "Go Developer",
            "Swift Developer", "Kotlin Developer", "Data Engineer", "Data Scientist",
            "Machine Learning Engineer", "AI Developer", "Big Data Developer",
            "DevOps Engineer", "Systems Engineer", "Site Reliability Engineer",
            "Network Engineer", "Cloud Engineer", "Infrastructure Engineer",
            "Database Administrator", "Database Developer", "Game Developer",
            "Game Designer", "Game Programmer", "QA Engineer", "Test Engineer",
            "Automation Engineer", "Technical Lead", "Team Lead", "Engineering Manager",
            "CTO", "VP of Engineering", "Development Manager", "Security Engineer",
            "Cybersecurity Specialist", "Application Security Engineer", "UI/UX Designer",
            "UI Developer", "UX Engineer", "소프트웨어 개발자", "소프트웨어 엔지니어", "풀스택 개발자",
            "백엔드 개발자", "프론트엔드 개발자", "모바일 개발자", "웹 개발자", "응용 프로그램 개발자",
            "자바 개발자", "파이썬 개발자", "자바스크립트 개발자", ".NET 개발자", "루비 온 레일즈 개발자",
            "PHP 개발자", "C++ 개발자", "Go 개발자", "스위프트 개발자", "코틀린 개발자",
            "데이터 엔지니어", "데이터 과학자", "머신러닝 엔지니어", "AI 개발자", "빅데이터 개발자",
            "DevOps 엔지니어", "시스템 엔지니어", "사이트 신뢰성 엔지니어", "네트워크 엔지니어",
            "클라우드 엔지니어", "인프라 엔지니어", "데이터베이스 관리자", "데이터베이스 개발자",
            "게임 개발자", "게임 디자이너", "게임 프로그래머", "품질 보증 엔지니어", "테스트 엔지니어",
            "자동화 엔지니어", "테크니컬 리드", "팀 리드", "엔지니어링 매니저", "최고 기술 책임자",
            "엔지니어링 부직급1", "개발 매니저", "보안 엔지니어", "사이버 보안 전문가",
            "애플리케이션 보안 엔지니어", "UI/UX 디자이너", "UI 개발자", "UX 엔지니어"
    };



}

package com.ssafy.s10p31s102be.profile.dto.request;

import com.ssafy.s10p31s102be.profile.infra.enums.Degree;
import com.ssafy.s10p31s102be.profile.infra.enums.EmployStatus;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileFilterSearchDto {
    private String searchString; // 검색어
    private List<String> column2; // column1
    private List<String> jobRanks; // 직급 Entity로 변환 가능
    private List<String> companyNames; // 기업명
    private List<Degree> degrees; // 학위
    private List<String> schoolNames; // 학교명
    private List<String> departmentNames; // 발굴사업부
    private List<String> founderNames; // 발굴담당자
    private Integer careerMinYear;  // 경력기간 Range 시작
    private Integer careerMaxYear; // 경력기간 Range 종료
    private List<String> techSkillKeywords; // 커리어 내 special 키워드들
    private Integer graduateMinYear; // 졸업년월 Range 시작
    private Integer graduateMaxYear; // 졸업년월 Range 끝
    private List<EmployStatus> employStatuses; // 진행단계
    private List<String> profileKeywords; // 프로필 키워드들
    private Boolean isMine; // 내가 발굴한 지원자들 검색
    private String skillSubCategory; // 기술 분야 기준 추천
    private List<Integer> exceptProfileIds; // 검색 대상에서 제외할 프로필 Id들
}

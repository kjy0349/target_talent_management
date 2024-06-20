package com.ssafy.s10p31s102be.admin.dto.request;

import com.ssafy.s10p31s102be.profile.infra.enums.Degree;
import com.ssafy.s10p31s102be.profile.infra.enums.EmployStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * column2;
 * jobranks
 * companyNames; // 태그 추가
 * 경력 기간 설정 그래프 기반
 * degrees 멀티 셀렉트
 * schoolNames; // 태그 추가
 * majors; // 전공들 추가
 * keywords // 태그 추가
 * gradudated // 졸업년월
 * foundDeptids // Dept 리스트 멀티 셀렉트
 * 발굴자 // member 리스트 멀티 셀렉트
 * 네트워킹 // member 리스트 멀티 셀렉트
 */
@Data
public class ProfileAdminFilterSearchDto {
    private String names;
    private List<String> column2;
    private List<String> jobRanks;
    private List<String> companyNames;
    private LocalDateTime careerStartedAt;
    private LocalDateTime careerEndedAt;
    private List<String> companyName;
    private List<Degree> degrees;
    private List<String> schoolNames;
    private List<String> majors;
    private List<String> keywords;
    private LocalDateTime graduatedAt;
    private List<String> employStatuses;
    private List<String> foundDeptNames;
    private List<String> founderNames;
    private List<String> networkingResponsibleMemberNames;
}

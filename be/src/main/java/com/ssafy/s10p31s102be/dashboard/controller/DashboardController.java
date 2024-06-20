package com.ssafy.s10p31s102be.dashboard.controller;

import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.dashboard.dto.request.DashboardCountrySearchConditionDto;
import com.ssafy.s10p31s102be.dashboard.dto.request.DashboardDepartmentSearchConditionDto;
import com.ssafy.s10p31s102be.dashboard.dto.request.DashboardMonthlySearchConditionDto;
import com.ssafy.s10p31s102be.dashboard.dto.response.DashboardCountrySummaryDto;
import com.ssafy.s10p31s102be.dashboard.dto.response.DashboardDepartmentFullDto;
import com.ssafy.s10p31s102be.dashboard.dto.response.DashboardMainContentFullDto;
import com.ssafy.s10p31s102be.dashboard.dto.response.DashboardMonthlyFullDto;
import com.ssafy.s10p31s102be.dashboard.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 *
 * TODO
 * Dashboard는 결국 데이터가 쌓이면 실시간으로 가면 사용자 경험면에서도 ( 최소 api 콜수 3 -> 다중 쿼리 ) 실제 서비스 측면에서도 별로니
 * 추후 Develop 방안으로 Scheduler를 달아서 통계테이블을 관리하며 이를 뽑아내는 식으로 개선할 필요가 있음.
 * 주석
 */

@RestController
@Tag( name = "대시보드" )
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashBoardService;
    @Operation(summary = "대시보드 상단 조회", description = "메인 페이지의 상단 통계를 집계받는 로직 -> ( 부서장 이하 | 본인 사업부 ) / ( 그외 | 전사 ) ")
    @GetMapping("/dashboard/main")
    public ResponseEntity<DashboardMainContentFullDto> getAllDashboardMainContent(@AuthenticationPrincipal UserDetailsImpl userDetails ){

        DashboardMainContentFullDto dashboardMainContentFullDto = dashBoardService.getDashboardMainContent( userDetails );
//        System.out.println(dashboardMainContentFullDto.toString());
        return ResponseEntity.ok( dashboardMainContentFullDto );
    }

    @Operation(summary = "대시보드 내부 인재풀 전체 갯수 조회", description = "시스템 인재풀 전체 갯수를 조회하는 로직")
    @GetMapping("/dashboard/sum-pool-size")
    public ResponseEntity<Long> getAllDashboardSumPoolSize(@AuthenticationPrincipal UserDetailsImpl userDetails ){
//        System.out.println(dashboardMainContentFullDto.toString());
        return ResponseEntity.ok(dashBoardService.getSumProfilePool( userDetails ) );
    }
    @Operation(summary = "대시보드 내부 네트워킹 전채 갯수 조회", description = "시스템 네트워킹 전체 갯수를 조회하는 로직")
    @GetMapping("/dashboard/networking")
    public ResponseEntity<Long> getAllNetworkingValue(){
        return ResponseEntity.ok(dashBoardService.getNetworkingValue());
    }

    @Operation(summary = "대시보드 사업부 별 그래프 통계 데이터 조회", description = "메인 페이지 내부 대시보드의 사업부 별 통계에 필요한 데이터를 집계하는 로직")
    @PostMapping("/dashboard/main/department")
    public ResponseEntity<DashboardDepartmentFullDto> getAllDashboardDepartment(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody DashboardDepartmentSearchConditionDto searchConditionDto ){

        DashboardDepartmentFullDto dashboardDepartmentFullDto = dashBoardService.getDashboardDepartment( userDetails,searchConditionDto );

        return ResponseEntity.ok( dashboardDepartmentFullDto );
    }
    @Operation(summary = "대시보드 월별 통계 데이터 조회", description = "메인 페이지 내부 대시보드의 월별 통계에 필요한 데이터를 집계하는 로직")
    @PostMapping("/dashboard/main/monthly")
    public ResponseEntity<DashboardMonthlyFullDto> getAllDashboardMonthly(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody DashboardMonthlySearchConditionDto searchConditionDto ){
        System.out.println("condition" + searchConditionDto.toString() );
        DashboardMonthlyFullDto dashboardMonthlyFullDto = dashBoardService.getDashboardMonthly( userDetails,searchConditionDto );

        return ResponseEntity.ok( dashboardMonthlyFullDto );
    }

    @Operation(summary = "대시보드 기술 분야별 통계 조회", description = "메인 페이지 내부 대시보드의 기술 분야별 통계를 집계하는 로직")
    @GetMapping("/dashboard/profile/skill")
    public ResponseEntity<List<String>> getAllDashboardProfileSkillMainCategory(@AuthenticationPrincipal UserDetailsImpl userDetails){

        List<String> skills = dashBoardService.getDashboardProfileSkillMainCategory( userDetails );
        return ResponseEntity.ok(skills);
    }
    @Operation(summary = "대시보드 국가 별 통계 조회", description = "메인 페이지 내부 대시보드의 국가 별 통계를 집계하는 로직")
    @PostMapping("/dashboard/main/country")
    public ResponseEntity<Map<String, DashboardCountrySummaryDto>>getAllDashboardCountry(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody DashboardCountrySearchConditionDto dashboardCountrySearchConditionDto ){
        System.out.println( dashboardCountrySearchConditionDto );
        Map<String,DashboardCountrySummaryDto> map = dashBoardService.getDashboardCountry( userDetails, dashboardCountrySearchConditionDto );
        System.out.println("result:" + map );
        return ResponseEntity.ok(map);
    }
}

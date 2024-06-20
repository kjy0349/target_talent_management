package com.ssafy.s10p31s102be.admin.controller;

import com.ssafy.s10p31s102be.admin.dto.request.*;
import com.ssafy.s10p31s102be.admin.dto.response.*;
import com.ssafy.s10p31s102be.admin.dto.response.member.DepartmentAdminSummaryDto;
import com.ssafy.s10p31s102be.profile.dto.request.LabCreateDto;
import com.ssafy.s10p31s102be.profile.infra.entity.education.Lab;
import com.ssafy.s10p31s102be.profile.infra.entity.education.School;
import com.ssafy.s10p31s102be.project.dto.response.ProjectFullDto;
import com.ssafy.s10p31s102be.project.dto.response.ProjectListFullDto;
import com.ssafy.s10p31s102be.admin.service.AdminService;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.member.infra.entity.*;
import com.ssafy.s10p31s102be.project.dto.request.ProjectSearchConditionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "관리자")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    private final AdminService adminService;

    @Operation(summary = "멤버 생성", description = "관리자의 사용자 생성 로직.")
    @PostMapping("/member")
    public ResponseEntity<Void> createMember(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody MemberAdminCreateDto dto) {
        adminService.createMember(userDetails, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "멤버 검색", description = "관리자의 필터 기반 사용자 조회 로직")
    @PostMapping("/member/search")
    public ResponseEntity<MemberSearchResultDto> getMembersWithFilters(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody( required = false ) MemberAdminSearchConditionDto memberAdminSearchConditionDto, @RequestParam( defaultValue = "0",name="page" ) Integer page, @RequestParam( defaultValue = "4", name="size" ) Integer size ){
        Pageable pageable = PageRequest.of( page, size );
        MemberSearchResultDto dtoList= adminService.getAllMembersByFilter( userDetails, memberAdminSearchConditionDto, pageable );
        return ResponseEntity.ok( dtoList );
    }

    @Operation(summary = "멤버 조회", description = "관리자의 사용자 조회 로직")
    @GetMapping("/member")
    public ResponseEntity<List<MemberAdminSummaryDto>> getMembers(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam(defaultValue = "0", name = "page") int page,
                                                                  @RequestParam(defaultValue = "3", name = "size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<MemberAdminSummaryDto> dtoList = adminService.getAllMembers(userDetails, pageable);
        return ResponseEntity.ok(dtoList);
    }



    @Operation(summary = "멤버 갯수 조회", description = "관리자의 사용자 인원 수 조회 로직")
    @GetMapping("/member/count")
    public ResponseEntity<Long> getMembersCount(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(adminService.getMemberCount(userDetails));
    }

    @Operation(summary = "멤버 사용량 통계 조회", description = "관리자의 사용량 통게 조회 로직")
    @GetMapping("/member/report")
    public ResponseEntity<MemberUsageChangeDto> getMemberUsageChanges( @AuthenticationPrincipal UserDetailsImpl userDetails ){
        return ResponseEntity.ok(adminService.getUsageChanges( userDetails ) );
    }
    @Operation(summary = "멤버 사용률 사용빈도 통계 조회", description = "관리자의 회원 사용률 사용빈도 조회 로직")
    @GetMapping("/member/report/static")
    public MemberAdminStaticDto getAdminMainPageStatics(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.getAdminMainPageStatics(userDetails);
    }
    @Operation(summary = "멤버 수정", description = "관리자의 회원 수정 로직")
    @PutMapping("/member/{targetId}")
    public ResponseEntity<Void> updateMember(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "targetId") Integer targetId, MemberAdminUpdateDto dto) {
        adminService.updateMember(userDetails, targetId, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "멤버 권한 수정", description = "관리자의 사용자 권한 수정 로직")
    @PutMapping("/member/authority")
    public ResponseEntity<Void> updateMemberAuthorities(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody MemberAdminUpdateAuthorityDto memberAdminUpdateAuthorityDto) {
        adminService.updateMemberAuthorities(userDetails, memberAdminUpdateAuthorityDto.getMemberIds(), memberAdminUpdateAuthorityDto.getAuthorityId());
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "멤버 삭제", description = "관리자의 사용자 삭제 로직")
    @DeleteMapping("/member/{targetId}")
    public ResponseEntity<Void> deleteMember(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "targetId") Integer targetId) {
        adminService.deleteMember(userDetails, targetId);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "멤버 일괄 삭제", description = "관리자의 사용자 일괄 삭제 로직")
    @PutMapping("/member")
    public ResponseEntity<Void> deleteMembers(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody MemberAdminDeleteDto dto) {
        adminService.deleteMembers(userDetails, dto);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "권한 생성", description = "관리자의 사용자 권한 생성 로직")
    @PostMapping("/authority")
    public ResponseEntity<AuthorityAdminSummaryDto> createAuthority(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody AuthorityAdminCreateDto dto) {
        System.out.println("auth," + dto.toString());
        return ResponseEntity.ok(AuthorityAdminSummaryDto.fromEntity(adminService.createAuthority(userDetails, dto)));
    }
    @Operation(summary = "권한 조회", description = "관리자의 권한 리스트 조회 로직")
    @GetMapping("/authority")
    public ResponseEntity<List<AuthorityAdminSummaryDto>> getAuthorities(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<Authority> authorities = adminService.getAllAuthorities(userDetails);
        List<AuthorityAdminSummaryDto> dtoList = authorities.stream().map(AuthorityAdminSummaryDto::fromEntity).toList();
        return ResponseEntity.ok(dtoList);
    }
    @Operation(summary = "권한 수정", description = "관리자의 권한 수정 로직")
    @PutMapping("/authority/{authorityId}")
    public ResponseEntity<Void> updateAuthority(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "authorityId") Integer authorityId, @RequestBody AuthorityAdminUpdateDto dto) {
        adminService.updateAuthority(userDetails, authorityId, dto);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "권한 삭제", description = "관리자의 권한 삭제 로직")
    @DeleteMapping("/authority/{authorityId}")
    public ResponseEntity<Void> deleteAuthority(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "authorityId") Integer authorityId) {
        adminService.deleteAuthority(userDetails, authorityId);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "사업부 생성", description = "관리자의 사업부 생성 로직")
    @PostMapping("/department")
    public ResponseEntity<DepartmentAdminSummaryDto> createDepartment(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody DepartmentAdminCreateDto dto) {

        return ResponseEntity.ok(DepartmentAdminSummaryDto.fromEntity(adminService.createDepartment(userDetails, dto)));
    }
    @Operation(summary = "사업부 조회", description = "관리자의 사업부 리스트 조회 로직")
    @GetMapping("/department")
    public ResponseEntity<List<DepartmentAdminSummaryDto>> getDepartments(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<Department> departments = adminService.getAllDepartments(userDetails);
        List<DepartmentAdminSummaryDto> dtoList = departments.stream().map(DepartmentAdminSummaryDto::fromEntity).toList();
        return ResponseEntity.ok(dtoList);
    }
    @Operation(summary = "사업부 수정", description = "관리자의 사업부 수정 로직")
    @PutMapping("/department/{departmentId}")
    public ResponseEntity<Void> updateDepartment(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "departmentId") Integer departmentId, @RequestBody DepartmentAdminUpdateDto dto) {
        adminService.updateDepartment(userDetails, departmentId, dto);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "사업부 삭제", description = "관리자의 사업부 삭제 로직")
    @DeleteMapping("/department/{departmentId}")
    public ResponseEntity<Void> deleteDepartment(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "departmentId") Integer departmentId) {
        adminService.deleteDepartment(userDetails, departmentId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "부서 생성", description = "관리자의 부서 생성 로직")
    @PostMapping("/team")
    public ResponseEntity<TeamAdminFullDto> createTeam(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody TeamAdminCreateDto dto) {
        return ResponseEntity.ok(TeamAdminFullDto.fromEntity(adminService.createTeam(userDetails, dto)));
    }
    @Operation(summary = "부서 조회", description = "관리자의 부서 리스트 조회 로직")
    @GetMapping("/team")
    public ResponseEntity<List<TeamAdminFullDto>> getTeams(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<Team> teams = adminService.getAllTeams(userDetails);
        List<TeamAdminFullDto> dtoList = teams.stream().map(TeamAdminFullDto::fromEntity).toList();
        return ResponseEntity.ok(dtoList);
    }
    @Operation(summary = "부서 수정", description = "관리자의 부서 수정 로직")
    @PutMapping("/team/{teamId}")
    public ResponseEntity<Void> updateTeam(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "teamId") Integer teamId, @RequestBody TeamAdminUpdateDto dto) {
        adminService.updateTeam(userDetails, teamId, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "부서 삭제", description = "관리자의 부서 삭제 로직")
    @DeleteMapping("/team/{teamId}")
    public ResponseEntity<Void> deleteTeam(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "teamId") Integer teamId) {
        adminService.deleteTeam(userDetails, teamId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "담당 업무 생성", description = "관리자의 담당 업무 생성 로직")
    @PostMapping("/role")
    public ResponseEntity<RoleAdminSummaryDto> createRole(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody RoleAdminCreateDto dto) {
        return ResponseEntity.ok(RoleAdminSummaryDto.fromEntity(adminService.createRole(userDetails, dto)));
    }
    @Operation(summary = "담당 업무 조회", description = "관리자의 담당 업무 리스트 조회 로직")
    @GetMapping("/role")
    public ResponseEntity<List<RoleAdminSummaryDto>> getRoles(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<Role> roles = adminService.getAllRoles(userDetails);
        List<RoleAdminSummaryDto> dtoList = roles.stream().map(RoleAdminSummaryDto::fromEntity).toList();
        return ResponseEntity.ok(dtoList);
    }
    @Operation(summary = "담당 업무 수정", description = "관리자의 담당 업무 수정 로직")
    @PutMapping("/role/{roleId}")
    public ResponseEntity<Void> updateRole(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "roleId") Integer roleId, @RequestBody RoleAdminUpdateDto dto) {
        adminService.updateRole(userDetails, roleId, dto);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "담당 업무 삭제", description = "관리자의 담당 업무 삭제 로직")
    @DeleteMapping("/role/{roleId}")
    public ResponseEntity<Void> deleteRole(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "roleId") Integer roleId) {
        adminService.deleteRole(userDetails, roleId);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "직급 조회", description = "관리자의 직급 리스트 로직")
    @GetMapping("/jobrank")
    public ResponseEntity<List<JobRankAdminSummaryDto>> getAllJobRanksForUpdate(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<JobRank> jobRanks = adminService.getAllJobRanks(userDetails);
        return ResponseEntity.ok(jobRanks.stream().map(JobRankAdminSummaryDto::fromEntity).toList());
    }

    //--------------프로필-------------

//    @GetMapping("/profile/preview")
//    public ResponseEntity< List<ProfilePreviewAdminSummaryDto>> getPreviewsNotNetworked(@AuthenticationPrincipal UserDetailsImpl userDetails ){
//        List<Profile> profiles = adminService.getAllProfilesNotNetworked( userDetails );
//        return ResponseEntity.ok(profiles.stream().map(ProfilePreviewAdminSummaryDto::fromEntity).toList());
//    }

    @Deprecated
    @Operation(summary = "프로필 필터 그래프 데이터 조회", description = "관리자의 프로필 필터에 들어가는 그래프 데이터 조회 로직")
    @GetMapping("/profile/filter/graph")
    public ResponseEntity<ProfileAdminFilterGraphFullDto> getProfileFilterGrapPollSizeata(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProfileAdminFilterGraphFullDto dto = adminService.getProfileGrapPollSizeata(userDetails);
        return ResponseEntity.ok(dto);
    }
    @Operation(summary = "프로필 담당자 수정", description = "관리자의 프로필 담당자 수정 로직")
    @PutMapping("/profile/executive")
    public ResponseEntity<Void> putProfileExecutive(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ProfileAdminUpdateExecutiveDto dto) {
        adminService.updateProfileExecutive(userDetails, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "프로필 검색", description = "관리자의 필터 기반 프로필 검색 로직")
    @PostMapping("/profile/search")
    public ResponseEntity<List<ProfilePreviewAdminSummaryDto>> getAllProfilePreviewByFilter(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam(defaultValue = "5") Integer size, @RequestParam(defaultValue = "0") Integer page, @RequestBody ProfileAdminFilterSearchDto profileAdminFilterSearchDto) {
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok().body(adminService.getAllProfilePreviewByFilter(userDetails, pageable, profileAdminFilterSearchDto));
    }


    @Operation(summary = "프로필 갯수 조회", description = "관리자의 인재 갯수 조회 로직")
    @GetMapping("/profile/count")
    public ResponseEntity<Integer> getProfileCount(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Integer count = adminService.getProfileCount(userDetails);
        return ResponseEntity.ok(count);
    }

    //--------------프로젝트-------------

    @Operation(summary = "프로젝트 갯수 조회", description = "관리자의 프로젝트 갯수 조회 로직")
    @GetMapping("/project/count")
    public ResponseEntity<Long> getProjectsCount(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(adminService.getProjectsCount(userDetails));
    }
    @Operation(summary = "프로젝트 검색", description = "관리자의 필터 기반 프로젝트 검색 로직")
    @PostMapping("/project/search")
    public ResponseEntity<ProjectListFullDto> getProjectsList(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ProjectSearchConditionDto searchConditionDto, @RequestParam(defaultValue = "0", name = "page") int page,
                                                              @RequestParam(defaultValue = "10", name = "size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        ProjectListFullDto dto = adminService.getAllProjectsWithFilter(userDetails, searchConditionDto, pageable);


        return ResponseEntity.ok().body(dto);
    }

    @Operation(summary = "프로젝트 상세 조회", description = "관리자의 프로젝트 상세 조회 로직")
    @GetMapping("/project/{projectId}")
    public ResponseEntity<ProjectFullDto> getProject(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "projectId") Integer projectId) {
        ProjectFullDto dto = adminService.getProjectById(userDetails, projectId);
        return ResponseEntity.ok(dto);
    }
    @Operation(summary = "프로젝트 수정", description = "관리자의 프로젝트 수정 로직")
    @PutMapping("/project/{projectId}")
    public ResponseEntity<Void> updateProject(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "projectId") Integer projectId, @RequestBody ProjectAdminUpdateDto dto) {
        adminService.updateProject(userDetails, projectId, dto);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "프로젝트 삭제", description = "관리자의 프로젝트 삭제 로직")
    @DeleteMapping("/project/{projectId}")
    public ResponseEntity<Void> deleteProject(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "projectId") Integer projectId) {
        adminService.deleteProject(userDetails, projectId);
        return ResponseEntity.ok().build();
    }



    //-------------------------------------전체 공지 관리 ---------------------------------

    @Deprecated
    @GetMapping("/hi")
    public ResponseEntity<Void> notiExternalScheduledTest() {
        return ResponseEntity.ok().build();
    }


    //------------------------------------전체 팝업 관리-----------------------------------
    @Operation(summary = "팝업 등록", description = "관리자는 팝업을 새로 등록할 수 있다. ")
    @PostMapping("/popup")
    public ResponseEntity<Void> createPopupMessage(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                   @RequestBody PopupAdminCreateDto popupAdminCreateDto) {
        adminService.createPopupMessage(userDetails, popupAdminCreateDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "팝업 리스트 조회", description = "관리자는 자신이 작성한 팝업 리스트를 확인할 수 있다.")
    @PostMapping("/popup/list")
    public ResponseEntity<PopupMessagePageDto> readPopupMessages(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                 @RequestBody PopupMessageFindDto popupMessageFindDto) {
        return ResponseEntity.ok().body(adminService.findPopupMessages(userDetails, popupMessageFindDto));
    }

    @Operation(summary = "팝업 메시지 상세 조회", description = "관리자는 자신이 작성한 팝업 메시지 내용을 조회할 수 있다.")
    @GetMapping("/popup/{popupMessageId}")
    public ResponseEntity<PopupMessageReadDto> readPopupMessage(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                @PathVariable("popupMessageId") Integer popupMessageId) {
        return ResponseEntity.ok().body(adminService.findPopupMessage(userDetails, popupMessageId));
    }

    @Operation(summary = "팝업 메시지 노출", description = "모든 멤버들은 로그인 시 등록되어있는 팝업 메시지를 확인 가능하다. ")
    @GetMapping("/popup/show")
    public ResponseEntity<PopupMessageReadDto> showPopupMessage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(adminService.showPopupMessages(userDetails));
    }

    @Operation(summary = "팝업 메시지 수정", description = "관리자는 자신이 작성한 팝업 메시지 내용을 수정할 수 있다.")
    @PutMapping("/popup/{popupMessageId}")
    public ResponseEntity<Void> updatePopupMessage(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                   @PathVariable("popupMessageId") Integer popupMessageId,
                                                   @RequestBody PopupAdminCreateDto popupAdminCreateDto) {
        adminService.updatePopupMessage(userDetails, popupMessageId, popupAdminCreateDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "팝업 메시지 삭제", description = "관리자는 자신이 작성한 팝업 메시지 내용을 삭제할 수 있다.")
    @DeleteMapping("/popup/{popupMessageId}")
    public ResponseEntity<Void> deletePopupMessage(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                   @PathVariable("popupMessageId") Integer popupMessageId){
        adminService.deletePopupMessage(userDetails, popupMessageId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "학교 생성", description = "관리자의 학교 생성 로직")
    @PostMapping("/school")
    public ResponseEntity<Void> postSchool( @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody SchoolCreateDto createDto ){

        adminService.createSchool( userDetails, createDto );

        return ResponseEntity.ok().build();

    }

    @Operation(summary = "학교 조회", description = "관리자의 학교 조회 로직")
    @GetMapping("/school")
    public ResponseEntity<List<SchoolFullDto>> getSchools( @AuthenticationPrincipal UserDetailsImpl userDetails ){
        List< School > schools = adminService.getAllSchools( userDetails );

        return ResponseEntity.ok( schools.stream().map( s -> SchoolFullDto.fromEntity( s ) ).toList() );
    }
    @Operation(summary = "학교 수정", description = "관리자의 학교 수정 로직")
    @PutMapping("/school/{schoolId}")
    public ResponseEntity<Void> updateSchool( @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable( name = "schoolId") Integer schoolId ,@RequestBody SchoolUpdateDto dto ){
        School school = adminService.updateSchool( userDetails, schoolId ,dto );
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "학교 삭제", description = "관리자의 학교 삭제 로직")
    @DeleteMapping("/school/{schoolId}")
    public ResponseEntity<Void> deleteSchool( @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable( name = "schoolId") Integer schoolId ){
        adminService.deleteSchool( userDetails, schoolId );
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "직급 생성", description = "관리자의 직급 생성 로직")
    @PostMapping("/jobrank")
    public ResponseEntity<Void> postJobRank( @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody JobRankCreateDto createDto ){

        JobRank jobRank = adminService.createJobRank( userDetails, createDto );

        return ResponseEntity.ok().build();

    }

    @Operation(summary = "직급 수정", description = "관리자의 직급 수정 로직")
    @PutMapping("/jobrank/{jobrankId}")
    public ResponseEntity<Void> updateJobRanks( @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable( name = "jobrankId") Integer jobrankId ,@RequestBody JobRankUpdateDto dto ){
        JobRank jobRank = adminService.updateJobRank( userDetails, jobrankId ,dto );
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "직급 삭제", description = "관리자의 직급 삭제 로직")
    @DeleteMapping("/jobrank/{jobrankId}")
    public ResponseEntity<Void> deleteJobRank( @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable( name = "jobrankId") Integer jobrankId ){
        adminService.deleteJobRank( userDetails, jobrankId );
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "랩실 생성", description = "관리자의 랩실 생성 로직")
    @PostMapping("/lab")
    public ResponseEntity<Void> postLab( @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody LabAdminCreateDto createDto ){

        Lab lab = adminService.createLab( userDetails, createDto );
        return ResponseEntity.ok().build();

    }

    @Operation(summary = "학교 ID 기반 랩실 조회", description = "관리자의 학교 ID 기반 랩실 리스트 조회 로직")
    @GetMapping("/school/{schoolId}/lab")
    public ResponseEntity<List<LabAdminFullDto>> getLabsBySchoolId( @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable( name = "schoolId") Integer schoolId ){
        List< Lab > labs = adminService.getAllLabsBySchoolId( userDetails, schoolId );

        return ResponseEntity.ok( labs.stream().map( l -> LabAdminFullDto.fromEntity( l.getSchool(),l ) ).toList() );
    }
    @Operation(summary = "랩실 조회", description = "관리자의 랩실 리스트 조회 로직")
    @GetMapping("/lab")
    public ResponseEntity<List<LabAdminFullDto>> getFullLabs( @AuthenticationPrincipal UserDetailsImpl userDetails ){
        List< Lab >  labs = adminService.getAllLabs( userDetails );

        return ResponseEntity.ok( labs.stream().map( l -> LabAdminFullDto.fromEntity( l.getSchool(),l ) ).toList() );
    }
    @Operation(summary = "랩실 ID 기반 랩실 수정", description = "관리자의 랩실 수정 로직")
    @PutMapping("/lab/{labId}")
    public ResponseEntity<Void> updateLabs( @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable( name = "labId") Integer labId, @RequestBody LabAdminUpdateDto dto ){
        Lab lab = adminService.updateLab( userDetails, labId ,dto );
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "랩실 학교 수정", description = "관리자의 특정 랩실의 학교 수정 로직")
    @PutMapping("/lab/{labId}/school")
    public ResponseEntity<Void> updateLabs( @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable( name = "schoolId") Integer schoolId, @PathVariable( name = "labId") Integer labId, @RequestBody LabAdminUpdateDto dto ){
        Lab lab = adminService.updateLabSchool( userDetails, labId ,dto );
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "랩실 삭제", description = "관리자의 랩실 삭제 로직")
    @DeleteMapping("/lab/{labId}")
    public ResponseEntity<Void> deleteLab( @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable( name = "labId") Integer labId ){
        adminService.deleteLab( userDetails, labId );
        return ResponseEntity.ok().build();
    }




}
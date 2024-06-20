package com.ssafy.s10p31s102be.project.controller;



import com.ssafy.s10p31s102be.admin.dto.response.ProfilePreviewAdminSummaryDto;
import com.ssafy.s10p31s102be.common.infra.enums.DomainType;
import com.ssafy.s10p31s102be.networking.dto.request.NetworkingExcelDto;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.service.ProfileService;
import com.ssafy.s10p31s102be.project.dto.request.*;
import com.ssafy.s10p31s102be.project.dto.response.ProjectFilterFullDto;
import com.ssafy.s10p31s102be.project.dto.response.ProjectFullDto;
import com.ssafy.s10p31s102be.project.dto.response.ProjectListFullDto;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/project")
@Tag( name = "프로젝트" )
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ProfileService profileService;
    @Operation(summary = "프로젝트 생성", description = "권한 별 프로젝트를 생성하는 로직")
    @PostMapping
    public ResponseEntity<ProjectCreateDto> createProject(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ProjectCreateDto createDto ){
        projectService.createProject( userDetails,createDto );
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "프로젝트 검색", description = "권한 별 프로젝트를 검색하는 로직")
    @PostMapping("/search")
    public ResponseEntity<ProjectListFullDto> getProjectsList(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ProjectSearchConditionDto searchConditionDto, @RequestParam(defaultValue = "0", name="page") int page,
                                                              @RequestParam(defaultValue = "10", name="size") int size ) {
        Pageable pageable = PageRequest.of( page, size );
        ProjectListFullDto dto = projectService.getAllProjectsWithFilter(userDetails, searchConditionDto, pageable );
        return ResponseEntity.ok().body( dto );
    }
    @Operation(summary = "프로젝트 복사", description = "권한 별 프로젝트를 복사하는 로직")
    @PostMapping("/copy/{projectId}")
    public ResponseEntity< Void > copyProject( @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable( name = "projectId" ) Integer projectId, @RequestBody ProjectCopyDto dto ){
        projectService.copyProject( userDetails, projectId, dto) ;
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "프로젝트 세부 조회", description = "권한 별 프로젝트 세부 정보를 조회하는 로직")
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectFullDto> getProject(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable( name = "projectId" ) Integer projectId ) {
        ProjectFullDto dto = projectService.findByIdWithProfiles(userDetails, projectId );
        return ResponseEntity.ok(dto);
    }
    @Operation(summary = "프로젝트 수정", description = "권한 별 프로젝트를 수정하는 로직")
    @PutMapping("/{projectId}")
    public ResponseEntity<Void> updateProject( @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable( name = "projectId" ) Integer projectId, @RequestBody ProjectUpdateDto dto) {
        System.out.println("update : " + dto );
        projectService.updateProject( userDetails, projectId, dto);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "프로젝트 북마크 수정", description = "권한 별 프로젝트의 북마크를 수정하는 로직")
    @PutMapping("/bookmark/{projectId}")
    public ResponseEntity<Void> updateProjectBookMark( @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable( name = "projectId" ) Integer projectId, @RequestBody ProjectUpdateBookmarkDto dto) {
        projectService.updateProjectBookMark( userDetails, projectId, dto);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "프로젝트 인재 수정", description = "권한 별 프로젝트 내의 인재를 수정하는 로직")
    @PutMapping("/profiles/{projectId}")
    public ResponseEntity<Void> updateProjectProfiles( @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable( name = "projectId" ) Integer projectId, @RequestBody ProjectUpdateDto dto){
        projectService.updateProfiles( userDetails, projectId, dto );
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "프로젝트 사용자 수정", description = "권한 별 자신의 프로젝트에 사용자를 추가하고 삭제하는 로직")
    @PutMapping("/members/{projectId}")
    public ResponseEntity<Void> updateProjectMembers( @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable( name="projectId") Integer projectId, @RequestBody ProjectUpdateDto dto){
        projectService.updateMembers( userDetails, projectId, dto );
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "프로젝트 삭제", description = "권한 별 프로젝트를 삭제하는 로직")
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable( name="projectId") Integer projectId) {
        projectService.deleteProject(userDetails, projectId);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "프로젝트 필터 값 조회", description = "권한 별 프로젝트의 필터 값들을 조회하는 로직")
    @GetMapping("/filters")
    public ResponseEntity<ProjectFilterFullDto> getFilterValues( @AuthenticationPrincipal UserDetailsImpl userDetails ){
        ProjectFilterFullDto dto = projectService.getAllFilterValue( userDetails );
        return ResponseEntity.ok( dto );
    }
    @Operation(summary = "프로젝트 제목 수정", description = "권한 별 프로젝트 상세 페이지 내부의 제목을 수정하는 로직")
    @PutMapping("/title/{projectId}")
    public ResponseEntity<Void> updateProjectTitle( @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable( name="projectId") Integer projectId, @RequestBody ProjectUpdateDto dto){
        projectService.updateTitle( userDetails, projectId, dto );
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "프로젝트 엑셀 다운로드", description = "권한 별 프로젝트의 엑셀을 다운로드 하는 로직")
    @PostMapping("/excel-download")
    public ResponseEntity<StreamingResponseBody> downloadExcel(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                               @RequestBody ProjectExcelDto projectExcelDto) throws IOException {
        ByteArrayResource excelFile = projectService.excelDownload(userDetails, projectExcelDto);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=data.xlsx")
                .body(outputStream -> outputStream.write(excelFile.getByteArray()));
    }



}

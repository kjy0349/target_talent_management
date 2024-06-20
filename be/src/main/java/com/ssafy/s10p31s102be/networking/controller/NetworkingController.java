package com.ssafy.s10p31s102be.networking.controller;

import com.ssafy.s10p31s102be.admin.service.AdminService;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.networking.dto.request.NetworkingExcelDto;
import com.ssafy.s10p31s102be.member.infra.entity.Department;
import com.ssafy.s10p31s102be.member.infra.entity.Executive;
import com.ssafy.s10p31s102be.networking.dto.request.*;
import com.ssafy.s10p31s102be.networking.dto.response.ExecutiveSearchResultDto;
import com.ssafy.s10p31s102be.networking.dto.response.ExecutiveSummaryDto;
import com.ssafy.s10p31s102be.networking.dto.response.NetworkingFilterFullDto;
import com.ssafy.s10p31s102be.networking.dto.response.NetworkingSearchResultDto;
import com.ssafy.s10p31s102be.networking.infra.entity.Networking;
import com.ssafy.s10p31s102be.networking.service.NetworkingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/networking")
@Tag( name = "네트워킹")
@RequiredArgsConstructor
//TODO dto naming 컨벤션
//TODO member 인가 협의
public class NetworkingController {
    private final NetworkingService networkingService;
    private final AdminService adminService;


    //----------------네트워킹---------------------
    //TODO 프로젝트 LISTFULLDTO 로직을 개선하자 필터만 바꾸는 방식이 더욱 가볍다.
    @Operation(summary = "네트워킹 생성", description = "권한 별 네트워킹을 생성하는 로직")
    @PostMapping
    public ResponseEntity<Integer> createNetworking(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody NetworkingCreateDto dto ) {
        Networking networking = networkingService.createNetworking( userDetails, dto );
        return ResponseEntity.ok( networking.getId() );
    }
    @Operation(summary = "네트워킹 검색", description = "필터와 권한에 기반한 네트워킹 검색 로직")
    @PostMapping("/search")
    public ResponseEntity<NetworkingSearchResultDto> getAllNetworking(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody NetworkingSearchCondition searchConditionDto, @RequestParam(defaultValue = "0", name="page") int page, @RequestParam(defaultValue = "4", name="size") int size) {
//        System.out.println(searchConditionDto.toString());
        Pageable pageable = PageRequest.of( page, size );
        NetworkingSearchResultDto findResult= networkingService.getAllNetworkingsWithFilter( userDetails, searchConditionDto,pageable );
        return ResponseEntity.ok( findResult );
    }
    @Operation(summary = "네트워킹 직급4 조회", description = "네트워킹에 참여할 직급4을 전체 조회하는 로직")
    @GetMapping("/executive")
    public ResponseEntity<List<ExecutiveSummaryDto>> getAllExecutives(@AuthenticationPrincipal UserDetailsImpl userDetails ){
        List<Executive> executives = networkingService.getAllExecutives( userDetails );
        return ResponseEntity.ok( executives.stream().map( ExecutiveSummaryDto::fromEntity ).toList());
    }
    @Operation(summary = "네트워킹 직급4 수정", description = "특정 네트워킹의 직급4을 수정하는 로직")
    @PutMapping("/executive/{networkingId}")
    public ResponseEntity<Void> putNetworkingExecutive(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody NetworkingUpdateDto dto, @PathVariable Integer networkingId ){
        networkingService.updateExecutive( userDetails,networkingId ,dto );
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "네트워킹 직급4 검색", description = "네트워킹에 참여할 직급4을 필터를 기반으로 검색하는 로직")
    @PostMapping("/executive/search")
    public ResponseEntity<ExecutiveSearchResultDto> getMembersWithFilters(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody( required = false ) ExecutiveSearchConditionDto executiveSearchConditionDto, @RequestParam( defaultValue = "0", name = "page" ) Integer page, @RequestParam( defaultValue = "4", name = "size" ) Integer size ){
        ;
        Pageable pageable = PageRequest.of( page, size );
        ExecutiveSearchResultDto dtoList= networkingService.getAllExecutiveByFilter( userDetails, executiveSearchConditionDto, pageable );
        return ResponseEntity.ok( dtoList );
    }

    @Operation(summary = "네트워킹 필터 값 조회", description = "네트워킹 페이지에 구성 과정에 필요한 필터 값들을 조회하는 로직")
    @GetMapping("/filters")
    public ResponseEntity<NetworkingFilterFullDto> getAllNetworkingFilters(@AuthenticationPrincipal UserDetailsImpl userDetails){
        List<Networking> networkings = networkingService.getAllNetworkings( userDetails );
        List<Department> departments = networkingService.getAllDepartments( userDetails );
        NetworkingFilterFullDto networkingFilterFullDto = NetworkingFilterFullDto.fromEntity( networkings,departments );

        return ResponseEntity.ok(networkingFilterFullDto);
    }

    @Operation(summary = "네트워킹 프로필 포함 생성", description = "네트워킹을 특정 프로필과 함께 생성하는 로직")
    @PostMapping("/profile/{profileId}")
    public ResponseEntity<Void> createNetworkingWithProfile( @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable( name = "profileId" ) Integer profileId, @RequestBody NetworkingCreateDto dto ){
        networkingService.createNetworkingWithProfile( userDetails, profileId, dto );
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "네트워킹 생성", description = "권한 별 네트워킹을 생성하는 로직")
    @PutMapping("/list")
    public ResponseEntity<Void> updateNetworkingList(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody List<NetworkingUpdateDto> dtos ) {
        networkingService.updateNetworking( userDetails, dtos );

        return ResponseEntity.ok().build();
    }
    @Operation(summary = "네트워킹 수정", description = "권한에 따라 네트워킹을 수정하는 로직")
    @PutMapping("/{networkingId}")
    public ResponseEntity<Void> updateNetworking( @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable( name = "networkingId" ) Integer networkingId, @RequestBody NetworkingUpdateDto dto ){
        networkingService.updateNetworking( userDetails, networkingId,dto );
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "네트워킹 인재 수정", description = "권한에 따른 네트워킹 대상 인재를 수정하는 로직")
    @PutMapping("/profile/{networkingId}")
    public ResponseEntity<Void> updateNetworkingProfile( @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable( name = "networkingId" ) Integer networkingId, @RequestBody NetworkingUpdateDto dto ){
        networkingService.updateNetworkingProfiles( userDetails, networkingId,dto );
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "네트워킹 일괄 삭제", description = "권한 별 네트워킹을 일괄 삭제하는 로직")
    @PutMapping("/all")
    public ResponseEntity<Void> deleteNetworkings(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody List<Integer> networkIds ){

        networkingService.deleteNetworkings( userDetails, networkIds );
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "네트워킹 삭제", description = "권한 별 네트워킹을 삭제하는 로직")
    @DeleteMapping("/{networkingId}")
    public ResponseEntity<Void> deleteNetworking(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable( name = "networkingId" ) Integer networkingId) {
        networkingService.deleteNetworking( userDetails, networkingId);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "네트워킹 제출", description = "권한 별 네트워킹을 제출하는 로직")
    @PostMapping("/submit")
    public ResponseEntity<Void> submitNetworking(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody NetworkingSubmitDto dto ) {
        networkingService.submitNetworking(userDetails,dto);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "네트워킹 승인", description = "권한 별 네트워킹을 승인하는 로직")
    @PostMapping("/{networkingId}/approve")
    public ResponseEntity<Void> approveNetworking(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable( name = "networkingId" ) Integer networkingId, @RequestParam( value = "notificationId") Integer notificationId ) {
        networkingService.approveNetworking(userDetails,networkingId, notificationId);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "네트워킹 승인 여부 검토", description = "권한 별 네트워킹의 제출 가능 여부를 검토하는 로직")
    @PostMapping("/submittable")
    public ResponseEntity<Boolean> checkNetworking(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody NetworkingSubmitDto dto ) {
        Boolean result = networkingService.checkSubmittableNetworking(userDetails,dto);
        return ResponseEntity.ok(result);
    }
    @Operation(summary = "네트워킹 엑셀 다운로드", description = "권한 별 네트워킹의 엑셀을 다운로드 하는 로직")
    @PostMapping("/excel-download")
    public ResponseEntity<StreamingResponseBody> downloadExcel(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                               @RequestBody NetworkingExcelDto networkingExcelDto) throws IOException {
        ByteArrayResource excelFile = networkingService.excelDownload(userDetails, networkingExcelDto);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=data.xlsx")
                .body(outputStream -> outputStream.write(excelFile.getByteArray()));
    }
}

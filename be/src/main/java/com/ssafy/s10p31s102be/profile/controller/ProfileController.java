package com.ssafy.s10p31s102be.profile.controller;

import com.ssafy.s10p31s102be.admin.dto.response.JobRankAdminSummaryDto;
import com.ssafy.s10p31s102be.common.infra.enums.DomainType;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.member.service.JobRankService;
import com.ssafy.s10p31s102be.profile.dto.request.*;
import com.ssafy.s10p31s102be.profile.dto.response.*;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.enums.EmployStatus;
import com.ssafy.s10p31s102be.profile.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;
    private final JobRankService jobRankService;

    @PostMapping
    @Operation(summary = "프로필 정보 등록", description = "인재 (프로필) 의 기본 인적사항을 등록하고, 프로필을 생성한다.")
    public ResponseEntity<ProfilePreviewDto> create(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody ProfileCreateDto profileCreateDto) {
        Profile profile = profileService.create(userDetails, profileCreateDto);
        return ResponseEntity.ok().body(ProfilePreviewDto.fromEntity(profile));
    }

    @GetMapping("/column")
    @Operation(summary = "프로필 인적 사항 동적 컬럼 조회", description = "프로필 인적 사항의 항목 데이터를 조회한다.")
    public ResponseEntity<ProfileColumnListDto> getProfileDynamicColumns() {
        return ResponseEntity.ok(ProfileColumnListDto.builder()
                .columns(profileService.getProfileDynamicColumns().stream().map(ProfileColumnDto::fromEntity).toList())
                .jobRanks(jobRankService.findAllJobRanks().stream().map(JobRankAdminSummaryDto::fromEntity).toList())
                .disabledColumns(profileService.getCountryDisabledColumnsMap())
                .build());
    }

    @GetMapping("/{profileId}")
    @Operation(summary = "프로필 상세정보 조회", description = "프로필의 상세정보를 조회한다.")
    public ResponseEntity<ProfileDetailDto> getProfileDetailById(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("profileId") Integer profileId) {
        return ResponseEntity.ok().body(profileService.getProfileDetailById(userDetails, profileId));
    }

    @PostMapping("/search")
    @Operation(
            summary = "프로필 검색",
            description = "필터를 이용해 프로필을 검색할 수 있다.<br>" +
            "<br>검색 대상이 되는 데이터는 총 8가지이다:<br>" +
                    "<ul>" +
                    "<li>한글이름</li>" +
                    "<li>영어이름</li>" +
                    "<li>회사명</li>" +
                    "<li>학교명</li>" +
                    "<li>전공</li>" +
                    "<li>프로필 키워드</li>" +
                    "<li>커리어 키워드</li>" +
                    "<li>학력 키워드</li>" +
                    "<li>special</li>" +
                    "<li>special 설명</li>" +
                    "</ul>" +
                    "<br>현재 검색어를 공백을 기준으로 자른 후 키워드로 모두 사용하고 있다, 예를 들어 '김삼성 AI'로 입력할 경우, 김삼성 AI 각각에 대해 검색 대상 필터링을 진행한다.<br>" +
                    "<br>따라서, 검색 대상 항목 중 김삼성이나 AI가 포함되어 있는 경우, 검색 대상이 된다<br>" +
                    "<br>먼저 검색어를 통해 위의 데이터를 기준으로 검색을 진행할 때, 권한 및 데이터에 따라 초기 검색 대상을 지정한다.<br>" +
                    "<br><strong>권한 체크</strong><br>" +
                    "<ul>" +
                    "<li>나만보기 프로필일 경우, Admin이나 프로필 담당자를 제외한 모든 사람은 해당 프로필을 검색할 수 없다.</li>" +
                    "<li>사업부 프로필일 경우(전사 아님), 운영진이나 Admin(AuthorityLevel이 2 이하)을 제외한 모든 사람들은 자신의 사업부 프로필만 검색할 수 있다.<br>" +
                    "이때, 이 사업부는 해당 프로필을 담당하고 있는 담당자를 기준으로 체크한다.</li>" +
                    "</ul>" +
                    "<br><strong>MyPool 체크</strong><br>" +
                    "내가 발굴한 프로필들을 보기 위해서, isMineCheck가 존재한다. 필터의 값 중 isMine을 true로 표시하면, 검색 대상을 내가 발굴한 프로필만으로 한정지을 수 있다.<br>" +
                    "<br>이렇게 초기 검색 대상을 지정한 후, 필터에 따라 프로필을 검색한다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
            }
    )
    public ResponseEntity<ProfileFilterResponseDto> getAllProfilePreviewByFilter(@AuthenticationPrincipal UserDetailsImpl userDetails, @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.ASC, page = 0) Pageable pageable, @RequestBody ProfileFilterSearchDto profileFilterSearchDto) {
        ProfileFilterSearchedDto allProfilePreviewByFilter = profileService.getAllProfilePreviewByFilter(userDetails, pageable, profileFilterSearchDto);
        ProfileFilterResponseDto profileFilterResponseDto = ProfileFilterResponseDto.builder()
                .profilePreviews(new PageImpl<>(allProfilePreviewByFilter.getFilteredProfiles().stream().map(ProfilePreviewDto::fromEntity).toList(),
                        pageable,
                        allProfilePreviewByFilter.getCount()))
                .careerRange(allProfilePreviewByFilter.getCareerRange())
                .graduateAtRange(allProfilePreviewByFilter.getGraduateAtRange())
                .founderDepartmentCounts(allProfilePreviewByFilter.getFounderDepartmentCounts())
                .employStatusCounts(allProfilePreviewByFilter.getEmployStatusCounts())
                .myDepartment(allProfilePreviewByFilter.getMyDepartment())
                .myProfileCount(allProfilePreviewByFilter.getMyProfileCount())
                .build();
        return ResponseEntity.ok().body(profileFilterResponseDto);
    }

    @PutMapping("/{profileId}")
    @Operation(summary = "프로필 인적사항 수정", description = "인재 (프로필)의 인적사항을 수정한다.")
    public ResponseEntity<Void> updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "profileId") Integer profileId, @Valid @RequestBody ProfileCreateDto dto) {
        profileService.update(userDetails, profileId, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @Operation(summary = "프로필 삭제", description = "인재 (프로필) 정보를 삭제한다.")
    public ResponseEntity<Void> deleteProfile(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody ProfileIdsRequestDto dto) {
        profileService.delete(userDetails, dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/manager")
    @Operation(summary = "프로필 담당자 수정", description = "인재 (프로필) 의 담당자를 변경한다.")
    public ResponseEntity<List<ProfilePreviewDto>> updateManager(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody ProfileManagerUpdateDto dto) {
        return ResponseEntity.ok(profileService.updateProfileManager(userDetails, dto).stream().map(ProfilePreviewDto::fromEntity).toList());
    }

    @PutMapping("/{profileId}/profile-image")
    @Operation(summary = "프로필 이미지 수정", description = "인재 (프로필) 의 이미지를 변경한다.")
    public ResponseEntity<Void> updateProfileImage(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "profileId") Integer profileId, @Valid @RequestBody ProfileImageUpdateDto dto) {
        profileService.updateProfileImage(userDetails, profileId, dto.getUrl());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/status")
    @Operation(summary = "프로필 활동단계 수정", description = "인재 (프로필) 의 활동단계를 변경한다.")
    public ResponseEntity<List<EmployStatus>> updateStatus(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody ProfileStatusUpdateDto dto) {
        return ResponseEntity.ok(profileService.updateProfileEmployStatus(userDetails, dto));
    }

    @PostMapping("/excel-upload")
    @Operation(
            summary = "엑셀 업로드",
            description = "엑셀 업로드를 진행한다.<br>" +
                    "프로필 업로드를 진행한 후, 성공/실패 개수를 세서 failCount, successCount로 내려준다. 업로드에 성공하기 위해서는, 여러 제약조건들이 있다.<br>" +
                    "<ol>" +
                    "<li>담당자 KnoxID는 DB에 존재해야 한다.</li>" +
                    "<li>Admin이 업로드하는 것이 아닌 이상, 담당자 KnoxID로 식별한 담당자와 업로드를 시도하는 사용자와 부서가 같아야 한다.</li>" +
                    "<li>타겟직급은 DB로 관리되고 있기 때문에, create에서 필수값으로 넣어주고 있다. 따라서 없을 경우 C2로 넣어준다. -> 없을 경우 안 넣는 것으로 변경 가능</li>" +
                    "<li>채용단계를 입력하지 않을 경우, 기본값으로 발굴 상태로 넣어준다.</li>" +
                    "<li>나만보기, 전사프로필 여부가 Y로 입력 될 경우, 나만보기 프로필 상태가 되고 이 외의 값일 경우 아닌 상태로 된다.</li>" +
                    "<li>모든 날짜 포맷팅은 yyyyMM 형식으로 넣어야 한다.</li>" +
                    "<li>학교나 회사와 같이 DB로 관리되고 있는 것들은, 찾아서 넣어줘야 하기 때문에 중복된 학교가 DB에 존재할 경우 실패한다. -> DB에 추후 학교이름, 회사이름에 Unique 설정을 걸어놔야 정상적으로 작동할 것이다.</li>" +
                    "</ol>",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
            }
    )
    public ResponseEntity<ProfileExcelImportResponseDto> uploadProfile(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestPart("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok().body(profileService.insertFromExcel(userDetails, file));
    }

    @PostMapping("/search-name")
    @Operation(
            summary = "이름 자동완성",
            description = "내가 볼 수 있는 프로필들을 대상으로, 검색어를 기준으로 한글/영어이름으로 자동완성을 수행한다.<br>" +
                    "검색된 이름들 중, 오름차순으로 정렬해 5개를 뽑아낸다.<br>" +
                    "<br><strong>권한 체크</strong><br>" +
                    "<ol>" +
                    "<li>관리자일 경우, 모든 프로필을 기준으로 자동완성을 제공한다.</li>" +
                    "<li>운영진일 경우, 나만보기 프로필을 제외한 전사/부서 프로필 모두를 대상으로 자동완성을 제공한다.</li>" +
                    "<li>일반 사용자일 경우, 나만보기 여부 체크 + 부서 체크가 진행된 결과에 대해 자동완성을 제공한다.</li>" +
                    "</ol>",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
            }
    )
    public ResponseEntity<List<String>> getSearchedNames(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ProfileNameSearchDto nameSearchDto) {
        return ResponseEntity.ok().body(profileService.getSearchedNames(userDetails, nameSearchDto));
    }

    @GetMapping("/{profileId}/recommend")
    @Operation(
            summary = "비슷한 후보자 추천",
            description = "타겟 프로필을 기준으로 주어진 조건에 대해 필터링을 진행해 비슷한 후보자를 추천한다.<br>" +
                    "현재는 추천이라기보다는 데이터 기반 필터링이라고 하는 게 더 좋을 듯하다. 필터링 조건은 다음과 같다.<br>" +
                    "<ol>" +
                    "<li>총 커리어 연도 기준 +- 5년</li>" +
                    "<li>상세 분야 동일 여부</li>" +
                    "<li>경력/학력 키워드 일치 여부 - 이 부분에 대해서는, 타겟 프로필과 한 개라도 겹치는 부분이 있으면 대상이 된다.</li>" +
                    "</ol>" +
                    "RDB의 검색 기능만으로는 일치율 여부를 판단할 수 없다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
            }
    )
    public ResponseEntity<List<ProfilePreviewDto>> getRecommendedProfiles(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "profileId") Integer profileId) {
        List<Profile> profiles = profileService.recommendSimilarProfiles(userDetails, profileId);
        return ResponseEntity.ok().body(profiles.stream().map(ProfilePreviewDto::fromEntity).toList());
    }

    @PutMapping("/{profileId}/status")
    @Operation(summary = "프로필 활동단계 수정 (단일)", description = "프로필 활동단계를 변경한다. (단일)")
    public ResponseEntity<Void> updateStatus(@PathVariable(name = "profileId") Integer profileId, @Valid @RequestBody ProfileStatusUpdateDto dto) {
        profileService.updateProfileEmployStatusById(profileId, dto.getStatus());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/domaintype/{domaintype}/{domainId}")
    public ResponseEntity<ProfileExternalSearchFullDto> getProfilesNotRelatedWithDomain(@AuthenticationPrincipal UserDetailsImpl userDetails
            , @RequestBody(required = false) ProfileExternalSearchConditionDto searchConditionDto
            , @PathVariable(name = "domainId") Integer domainId
            , @PathVariable(name = "domaintype") DomainType domainType
            , @RequestParam(value = "page", defaultValue = "0") Integer page
            , @RequestParam(value = "size", defaultValue = "6") Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Profile> profiles = profileService.findForSelection(userDetails, domainId, domainType, searchConditionDto, pageable);


        return ResponseEntity.ok(ProfileExternalSearchFullDto.builder()
                .profileList(profiles.stream().map(ProfilePreviewDto::fromEntity).collect(Collectors.toList()))
                .maxCount(profiles.getTotalElements())
                .maxPage(profiles.getTotalPages())
                .build()
        );
    }
}

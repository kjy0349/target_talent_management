package com.ssafy.s10p31s102be.profile.controller;

import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.profile.dto.request.EducationCreateDto;
import com.ssafy.s10p31s102be.profile.dto.response.EducationDetailDto;
import com.ssafy.s10p31s102be.profile.infra.entity.education.Education;
import com.ssafy.s10p31s102be.profile.service.EducationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile/{profileId}/education")
public class EducationController {

    private final EducationService educationService;

    @PostMapping
    @Operation(
            summary = "학력 생성",
            description = "주어진 입력값에 따라 학력을 생성한다.<br>" +
                    "<br><strong>제약조건</strong><br>" +
                    "<ol>" +
                    "<li>학력을 생성할 타겟 프로필이 존재하지 않으면 안 된다.</li>" +
                    "<li>AuthorityLevel이 최소 5는 되어야 수행 가능하다. 권한 레벨이 2 이상인 경우(Admin, 운영진이 아닌 경우) 자신이 속한 부서 프로필의 학력만 생성할 수 있다.</li>" +
                    "<li>학력 시작일이 존재해야 한다. 하지만 종료일은 존재하지 않아도 상관없다. 존재하지 않을 경우 현재 시간을 대신 사용함.</li>" +
                    "</ol>" +
                    "<br><strong>추가사항</strong><br>" +
                    "학교명, 연구실명으로 DB에서 검색해 생성하는데, 없을 경우 새롭게 학교/연구실 데이터를 DB에 저장한다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
            }
    )
    public ResponseEntity<EducationDetailDto> create(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "profileId") Integer profileId, @Valid @RequestBody EducationCreateDto dto) {
        return ResponseEntity.ok(EducationDetailDto.fromEntity(educationService.create(userDetails, profileId, dto)));
    }

    @PutMapping("/{educationId}")
    @Operation(summary = "학력 수정", description = "주어진 입력값에 따라 학력을 수정한다." +
            "권한 체크는 생성과 동일하게 적용된다.")
    public ResponseEntity<EducationDetailDto> update(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "educationId") Long educationId, @Valid @RequestBody EducationCreateDto dto) {
        return ResponseEntity.ok(EducationDetailDto.fromEntity(educationService.update(userDetails, educationId, dto)));
    }

    @DeleteMapping("/{educationId}")
    @Operation(summary = "학력 삭제", description = "타겟 학력을 삭제한다." +
            "권한 체크는 생성과 동일하게 적용된다.")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "educationId") Long educationId) {
        educationService.delete(userDetails, educationId);
        return ResponseEntity.ok().build();
    }
}

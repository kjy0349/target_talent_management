package com.ssafy.s10p31s102be.profile.controller;

import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.profile.dto.request.CareerCreateDto;
import com.ssafy.s10p31s102be.profile.dto.response.CareerDetailDto;
import com.ssafy.s10p31s102be.profile.service.CareerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile/{profileId}/career")
public class CareerController {

    private final CareerService careerService;

    @PostMapping
    @Operation(
            summary = "경력 생성",
            description = "주어진 입력값에 따라 경력을 생성한다.<br>" +
                    "<br><strong>제약조건</strong><br>" +
                    "<ol>" +
                    "<li>경력을 생성할 타겟 프로필이 존재하지 않으면 안 된다.</li>" +
                    "<li>AuthorityLevel이 최소 5는 되어야 수행 가능하다. 권한 레벨이 2 이상인 경우(Admin, 운영진이 아닌 경우) 자신이 속한 부서 프로필의 경력만 생성할 수 있다.</li>" +
                    "<li>경력 시작일이 존재해야 한다. 하지만 종료일은 존재하지 않아도 상관없다. 존재하지 않을 경우 현재 시간을 대신 사용함.</li>" +
                    "</ol>" +
                    "<br><strong>추가사항</strong><br>" +
                    "회사명으로 DB에서 검색해 생성하는데, 없을 경우 새롭게 회사 데이터를 DB에 저장한다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
            }
    )
    public ResponseEntity<CareerDetailDto> create(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "profileId") Integer profileId, @Valid @RequestBody CareerCreateDto dto) {
        return ResponseEntity.ok(CareerDetailDto.fromEntity(careerService.create(userDetails, profileId, dto)));
    }

    @PutMapping("/{careerId}")
    @Operation(summary = "경력 수정", description = "값에 따라 경력을 수정한다. 생성과 동일하게 작동한다.")
    public ResponseEntity<CareerDetailDto> update(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "careerId") Long careerId, @Valid @RequestBody CareerCreateDto dto) {
        return ResponseEntity.ok(CareerDetailDto.fromEntity(careerService.update(userDetails, careerId, dto)));
    }

    @DeleteMapping("/{careerId}")
    @Operation(summary = "경력 삭제", description = "경력을 삭제한다. 생성과 동일한 권한체크 로직이 적용된다.")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "careerId") Long careerId) {
        careerService.delete(userDetails, careerId);
        return ResponseEntity.ok().build();
    }
}
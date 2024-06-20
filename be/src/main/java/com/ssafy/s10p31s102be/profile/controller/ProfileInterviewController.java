package com.ssafy.s10p31s102be.profile.controller;

import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.profile.dto.request.InterviewCreateDto;
import com.ssafy.s10p31s102be.profile.dto.request.InterviewUpdateDto;
import com.ssafy.s10p31s102be.profile.dto.response.InterviewDetailDto;
import com.ssafy.s10p31s102be.profile.service.InterviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile/{profileId}/interview")
public class ProfileInterviewController {
    private final InterviewService interviewService;

    @PostMapping
    @Operation(
            summary = "면접을 생성한다.",
            description = "면접을 생성한다.<br>" +
                    "<br><strong>제약조건</strong><br>" +
                    "<ol>" +
                    "<li>타겟 프로필이 존재해야 한다.</li>" +
                    "<li>AuthorityLevel이 최소 5는 되어야 수행 가능하다. 권한 레벨이 2 이상인 경우(Admin, 운영진이 아닌 경우) 자신이 속한 부서의 프로필에서만 면접을 생성할 수 있다.</li>" +
                    "</ol>",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
            }
    )
    public ResponseEntity<InterviewDetailDto> create(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("profileId") Integer profileId, @Valid @RequestBody InterviewCreateDto dto) {
        InterviewDetailDto interviewDetailDto = InterviewDetailDto.fromEntity(interviewService.create(userDetails, profileId, dto));
        return ResponseEntity.ok().body(interviewDetailDto);
    }

    @PutMapping("/{interviewId}")
    @Operation(summary = "면접을 수정한다.", description = "면접을 수정한다.\n" +
            "제약조건은 생성과 같다.")
    public ResponseEntity<InterviewDetailDto> update(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("interviewId") Integer interviewId, @Valid @RequestBody InterviewCreateDto dto) {
        return ResponseEntity.ok().body(InterviewDetailDto.fromEntity(interviewService.update(userDetails, interviewId, dto)));
    }

    @PutMapping("/{interviewId}/favorite")
    @Operation(summary = "면접 즐겨찾기를 업데이트한다.", description = "면접 즐겨찾기를 업데이트한다.\n" +
            "제약조건은 생성과 같다.")
    public ResponseEntity<InterviewDetailDto> updateFavorite(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("interviewId") Integer interviewId, @RequestBody Boolean isFavorite) {
        return ResponseEntity.ok(InterviewDetailDto.fromEntity(interviewService.updateFavorite(userDetails, interviewId, isFavorite)));
    }

    @DeleteMapping("/{interviewId}")
    @Operation(summary = "면접 을 삭제한다", description = "면접을 삭제한다.\n" +
            "제약조건은 생성과 같다.")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("interviewId") Integer interviewId) {
        interviewService.delete(userDetails, interviewId);
        return ResponseEntity.ok().build();
    }
}

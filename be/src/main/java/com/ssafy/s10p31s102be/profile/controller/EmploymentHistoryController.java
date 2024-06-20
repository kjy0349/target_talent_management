package com.ssafy.s10p31s102be.profile.controller;

import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.profile.dto.request.EmploymentHistoryCreateDto;
import com.ssafy.s10p31s102be.profile.dto.response.EmploymentHistoryDetailDto;
import com.ssafy.s10p31s102be.profile.dto.response.InterviewDetailDto;
import com.ssafy.s10p31s102be.profile.service.EmploymentHistoryService;
import com.ssafy.s10p31s102be.profile.service.InterviewService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile/{profileId}/employment-history")
public class EmploymentHistoryController {

    private final EmploymentHistoryService employmentHistoryService;
    private final InterviewService interviewService;

    @PostMapping
    @Operation(summary = "채용 전형 기록 생성", description = "채용 전형 (면접, 처우협의 등) 기록을 생성한다. RequestBody는 type에 따라 다르다.")
    public ResponseEntity<EmploymentHistoryDetailDto> create(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "profileId") Integer profileId, @Valid @RequestBody EmploymentHistoryCreateDto dto) {
        return ResponseEntity.ok(EmploymentHistoryDetailDto.fromEntity(employmentHistoryService.create(userDetails, profileId, dto)));
    }

    @GetMapping
    @Operation(summary = "채용 전형 기록 조회", description = "채용 전형을 모아서 조회한다.")
    public ResponseEntity<List<Object>> readEmploymentHistoriesWithInterviews(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "profileId") Integer profileId) {
        List<Object> results = new ArrayList<>();

        results.addAll(employmentHistoryService.readEmploymentHistories(userDetails, profileId).stream().map(EmploymentHistoryDetailDto::fromEntity).toList());
        results.addAll(interviewService.findAllInterviewByProfileId(userDetails, profileId).stream().map(InterviewDetailDto::fromEntity).toList());

        return ResponseEntity.ok(results);
    }

    @PutMapping("/{employmentHistoryId}")
    @Operation(summary = "채용 전형 기록 수정", description = "채용 전형 기록을 수정한다.")
    public ResponseEntity<EmploymentHistoryDetailDto> update(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "employmentHistoryId") Long employmentHistoryId, @Valid @RequestBody EmploymentHistoryCreateDto dto) {
        return ResponseEntity.ok(EmploymentHistoryDetailDto.fromEntity(employmentHistoryService.update(userDetails, employmentHistoryId, dto)));
    }

    @PutMapping("/{employmentHistoryId}/favorite")
    @Operation(summary = "채용 전형 즐겨찾기 설정", description = "채용 전형의 즐겨찾기를 토글 설정한다.")
    public ResponseEntity<EmploymentHistoryDetailDto> update(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "employmentHistoryId") Long employmentHistoryId, @RequestBody Boolean isFavorite) {
        return ResponseEntity.ok(EmploymentHistoryDetailDto.fromEntity(employmentHistoryService.updateFavorite(userDetails, employmentHistoryId, isFavorite)));
    }

    @DeleteMapping("/{employmentHistoryId}")
    @Operation(summary = "채용 전형 기록 삭제", description = "채용 전형 기록을 삭제한다.")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "employmentHistoryId") Long employmentHistoryId) {
        employmentHistoryService.delete(userDetails, employmentHistoryId);
        return ResponseEntity.ok().build();
    }
}

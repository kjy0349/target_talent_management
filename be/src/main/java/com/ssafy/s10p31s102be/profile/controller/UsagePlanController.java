package com.ssafy.s10p31s102be.profile.controller;

import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.profile.dto.request.UsagePlanCreateDto;
import com.ssafy.s10p31s102be.profile.dto.response.UsagePlanReadDto;
import com.ssafy.s10p31s102be.profile.infra.entity.community.UsagePlan;
import com.ssafy.s10p31s102be.profile.service.UsagePlanService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile/{profileId}/usage-plan")
public class UsagePlanController {

    private final UsagePlanService usagePlanService;

    @PostMapping
    @Operation(summary = "프로필 활용계획 생성", description = "프로필의 활용계획을 생성한다.")
    public ResponseEntity<UsagePlanReadDto> create(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "profileId") Integer profileId, @Valid @RequestBody UsagePlanCreateDto dto) {
        return ResponseEntity.ok(UsagePlanReadDto.fromEntity(usagePlanService.create(userDetails, profileId, dto)));
    }

    @GetMapping
    @Operation(summary = "프로필 활용계획 조회", description = "프로필의 활용계획을 조회한다.")
    public ResponseEntity<List<UsagePlanReadDto>> readUsagePlans(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "profileId") Integer profileId) {
        return ResponseEntity.ok(usagePlanService.readUsagePlans(userDetails, profileId).stream().map(UsagePlanReadDto::fromEntity).toList());
    }

    @PutMapping("/{usagePlanId}")
    @Operation(summary = "프로필 활용계획 수정", description = "프로필의 활용계획을 수정한다.")
    public ResponseEntity<UsagePlanReadDto> update(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "usagePlanId") Integer usagePlanId, @Valid @RequestBody UsagePlanCreateDto dto) {
        return ResponseEntity.ok(UsagePlanReadDto.fromEntity(usagePlanService.update(userDetails, usagePlanId, dto)));
    }

    @PutMapping("/{usagePlanId}/favorite")
    @Operation(summary = "프로필 활용계획 즐겨찾기 설정", description = "프로필 활용계획의 즐겨찾기를 토글 설정한다.")
    public ResponseEntity<UsagePlanReadDto> updateFavorite(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "usagePlanId") Integer usagePlanId, @RequestBody Boolean isFavorite) {
        return ResponseEntity.ok(UsagePlanReadDto.fromEntity(usagePlanService.updateFavorite(userDetails, usagePlanId, isFavorite)));
    }

    @DeleteMapping("/{usagePlanId}")
    @Operation(summary = "프로필 활용계획 삭제", description = "프로필 활용계획을 삭제한다.")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "usagePlanId") Integer usagePlanId) {
        usagePlanService.delete(userDetails, usagePlanId);
        return ResponseEntity.ok().build();
    }
}

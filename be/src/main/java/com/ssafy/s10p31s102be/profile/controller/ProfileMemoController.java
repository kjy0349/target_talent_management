package com.ssafy.s10p31s102be.profile.controller;

import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.profile.dto.request.ProfileMemoCreateDto;
import com.ssafy.s10p31s102be.profile.dto.response.ProfileMemoReadDto;
import com.ssafy.s10p31s102be.profile.service.ProfileMemoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile/{profileId}/memo")
public class ProfileMemoController {
    private final ProfileMemoService profileMemoService;

    @PostMapping
    @Operation(summary = "프로필 메모 생성", description = "프로필의 메모를 생성한다.")
    public ResponseEntity<ProfileMemoReadDto> create(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "profileId") Integer profileId, @Valid @RequestBody ProfileMemoCreateDto dto) {
        return ResponseEntity.ok(ProfileMemoReadDto.fromEntity(profileMemoService.create(userDetails, profileId, dto)));
    }

    @GetMapping
    @Operation(summary = "프로필 메모 조회", description = "프로필의 메모를 조회한다.")
    public ResponseEntity<List<ProfileMemoReadDto>> readProfileMemos(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "profileId") Integer profileId) {
        return ResponseEntity.ok(profileMemoService.readProfileMemos(userDetails, profileId).stream().map(ProfileMemoReadDto::fromEntity).toList());
    }

    @PutMapping("/{profileMemoId}")
    @Operation(summary = "프로필 메모 수정", description = "프로필의 메모를 수정한다.")
    public ResponseEntity<ProfileMemoReadDto> update(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "profileMemoId") Long profileMemoId, @Valid @RequestBody ProfileMemoCreateDto dto) {
        return ResponseEntity.ok(ProfileMemoReadDto.fromEntity(profileMemoService.update(userDetails, profileMemoId, dto)));
    }

    @DeleteMapping("/{profileMemoId}")
    @Operation(summary = "프로필 메모 삭제", description = "프로필의 메모를 삭제한다.")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "profileMemoId") Long profileMemoId) {
        profileMemoService.delete(userDetails, profileMemoId);
        return ResponseEntity.ok().build();
    }
}

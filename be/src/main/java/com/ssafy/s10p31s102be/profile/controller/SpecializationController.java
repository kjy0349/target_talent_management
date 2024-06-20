package com.ssafy.s10p31s102be.profile.controller;

import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.profile.dto.request.SpecializationCreateDto;
import com.ssafy.s10p31s102be.profile.dto.response.SpecializationDetailDto;
import com.ssafy.s10p31s102be.profile.infra.entity.career.Specialization;
import com.ssafy.s10p31s102be.profile.service.SpecializationService;
import io.swagger.v3.oas.annotations.Operation;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile/{profileId}/specialization")
public class SpecializationController {
    private final SpecializationService specializationService;

    @PostMapping
    @Operation(summary = "special 생성", description = "주어진 입력값에 따라 special를 생성한다.\n" +
            "제약조건\n" +
            "1. 경력을 생성할 타겟 프로필이 존재하지 않으면 안된다.\n" +
            "2. AuthorityLevel이 최소 5는 되어야 수행 가능하다. 권한 레벨이 2 이상인경우(Admin, 운영진이 아닌 경우) 자신이 속한 부서 프로필의 special만 생성할 수 있다."
    )
    public ResponseEntity<SpecializationDetailDto> create(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "profileId") Integer profileId, @RequestBody SpecializationCreateDto specializationCreateDto) {
        Specialization specialization = specializationService.create(userDetails, profileId, specializationCreateDto);
        return ResponseEntity.ok().body(SpecializationDetailDto.fromEntity(specialization));
    }

    @GetMapping
    @Operation(summary = "special 읽기", description = "special를 읽어온다.\n" +
            "제약조건은 생성과 같다.")
    public ResponseEntity<List<SpecializationDetailDto>> readSpecializations(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "profileId") Integer profileId) {
        return ResponseEntity.ok().body(specializationService.readSpecializations(userDetails, profileId).stream().map(SpecializationDetailDto::fromEntity).toList());
    }

    @PutMapping("/{specializationId}")
    @Operation(summary = "special 수정", description = "special를 수정한다.\n" +
            "제약조건은 생성과 같다.")
    public ResponseEntity<SpecializationDetailDto> update(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "specializationId") Long specializationId, @RequestBody SpecializationCreateDto specializationCreateDto) {
        return ResponseEntity.ok().body(SpecializationDetailDto.fromEntity(specializationService.update(userDetails, specializationId, specializationCreateDto)));
    }

    @PutMapping("/{specializationId}/favorite")
    @Operation(summary = "special 즐겨찾기 업데이트", description = "special 즐겨찾기를 업데이트한다.\n" +
            "제약조건은 생성과 같다.")
    public ResponseEntity<SpecializationDetailDto> updateFavorite(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "specializationId") Long specializationId, @RequestBody Boolean isFavorite) {
        return ResponseEntity.ok(SpecializationDetailDto.fromEntity(specializationService.updateFavorite(userDetails, specializationId, isFavorite)));
    }

    @DeleteMapping("/{specializationId}")
    @Operation(summary = "special 삭제", description = "special를 삭제한다.\n" +
            "제약조건은 생성과 같다.")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "specializationId") Long specializationId) {
        specializationService.delete(userDetails, specializationId);
        return ResponseEntity.ok().build();
    }

}

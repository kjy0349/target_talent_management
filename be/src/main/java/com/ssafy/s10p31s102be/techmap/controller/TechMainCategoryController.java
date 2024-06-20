package com.ssafy.s10p31s102be.techmap.controller;

import com.ssafy.s10p31s102be.techmap.dto.response.TechMainCategoryReadDto;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechMainCategory;
import com.ssafy.s10p31s102be.techmap.service.TechMainCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "기술 분야 정보")
public class TechMainCategoryController {
    private final TechMainCategoryService techMainCategoryService;

    /*
        신규 기술 관련 대분류
     */
    @Operation(summary = "기술 분야 조회", description = "서비스 내 등록되어있는 기술분야 조회")
    @GetMapping("/tech-main-category")
    public ResponseEntity<List<TechMainCategoryReadDto>> findTechMainCategories(){

        List<TechMainCategoryReadDto> techMainCategories = techMainCategoryService.findTechMainCategories().stream()
                .map(TechMainCategoryReadDto::fromEntity).toList();

        return ResponseEntity.ok().body(techMainCategories);
    }
}

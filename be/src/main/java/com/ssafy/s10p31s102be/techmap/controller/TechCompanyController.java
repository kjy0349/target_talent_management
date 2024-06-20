package com.ssafy.s10p31s102be.techmap.controller;

import com.ssafy.s10p31s102be.techmap.dto.response.TechCompanyReadDto;
import com.ssafy.s10p31s102be.techmap.service.TechCompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tech-company")
@Tag(name = "신규 기술 분야 회사")
public class TechCompanyController {
    private final TechCompanyService techCompanyService;

    @Operation(summary = "회사 조회", description = "해당 신규 기술 분야에 등록되어있는 회사 정보를 불러온다.")
    @GetMapping("/{techmapProjectId}")
    public ResponseEntity<List<TechCompanyReadDto>> findTechCompanies(@PathVariable("techmapProjectId") Integer techmapProjectId) {
        return ResponseEntity.ok().body(techCompanyService.readTechCompanies(techmapProjectId).stream().map(TechCompanyReadDto::fromEntity).toList());
    }

    @Operation(summary = "회사 생성 및 업데이트", description = "해당 신규 기술 분야에 등록된 회사 정보를 수정한다.")
    @PostMapping("/{techmapProjectId}")
    public ResponseEntity<Void> updateTechCompanies(@PathVariable("techmapProjectId") Integer techmapProjectId, @RequestBody List<Integer> companies) {
        techCompanyService.updateTechCompanies(techmapProjectId, companies);

        return ResponseEntity.ok().build();
    }
}

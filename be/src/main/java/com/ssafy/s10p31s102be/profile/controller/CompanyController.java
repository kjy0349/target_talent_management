package com.ssafy.s10p31s102be.profile.controller;

import com.ssafy.s10p31s102be.profile.dto.request.CompanyCreateDto;
import com.ssafy.s10p31s102be.profile.dto.response.CompanyDetailDto;
import com.ssafy.s10p31s102be.profile.infra.entity.career.Company;
import com.ssafy.s10p31s102be.profile.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회사")
@RestController
@RequiredArgsConstructor
@RequestMapping("/company")
public class CompanyController {
    private final CompanyService companyService;

    @Operation(summary = "신규 회사 등록", description = "새로운 회사를 등록 가능하다.")
    @PostMapping
    public ResponseEntity<CompanyDetailDto> createCompany(@RequestBody CompanyCreateDto companyCreateDto){
        Company company = companyService.companyCreate(companyCreateDto);

        return ResponseEntity.ok().body(CompanyDetailDto.builder()
                .companyId(company.getId())
                .name(company.getName())
                .build());
    }
}

package com.ssafy.s10p31s102be.profile.controller;

import com.ssafy.s10p31s102be.profile.dto.request.LabCreateDto;
import com.ssafy.s10p31s102be.profile.dto.response.TechLabDto;
import com.ssafy.s10p31s102be.profile.infra.entity.education.Lab;
import com.ssafy.s10p31s102be.profile.service.LabService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Tag(name = "연구실")
@RestController
@RequiredArgsConstructor
@RequestMapping("/lab")
public class LabController {
    private final LabService labService;

    @Operation(summary = "신규 연구실 등록", description = "새로운 연구실을 등록 가능하다.")
    @PostMapping
    public ResponseEntity<TechLabDto> createLab(@RequestBody LabCreateDto labCreateDto) {

        Lab lab = labService.createLab(labCreateDto);

        return ResponseEntity.ok().body(TechLabDto.builder()
                .labId(lab.getLabId())
                .schoolName(lab.getSchool().getSchoolName())
                .major(lab.getMajor())
                .labName(lab.getLabName())
                .labProfessor(lab.getLabProfessor())
                .build());
    }
}

package com.ssafy.s10p31s102be.techmap.controller;

import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.techmap.dto.response.TechLabReadDto;
import com.ssafy.s10p31s102be.techmap.service.TechLabService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Tech-lab")
@Tag(name = "신규 기술 분야 연구실")
public class TechLabController {
    private final TechLabService techLabService;

    @Operation(summary = "연구실 자동완성", description = "서비스 내 등록되어있는 연구실에 대한 자동완성")
    @GetMapping("/search")
    public ResponseEntity<List<TechLabReadDto>> searchTechLab(@RequestParam("word") String word) {
        return ResponseEntity.ok().body(techLabService.searchTechLabs(word));
    }

    @Operation(summary = "연구실 조회", description = "해당 신규 기술 분야에 등록되어있는 연구실 정보를 불러온다.")
    @GetMapping("/{techmapProjectId}")
    public ResponseEntity<List<TechLabReadDto>> findTechLabs(@PathVariable("techmapProjectId") Integer techmapProjectId) {
        return ResponseEntity.ok().body(techLabService.readTechLabs(techmapProjectId).stream().map(TechLabReadDto::fromEntity).toList());
    }

    @Operation(summary = "연구실 조회", description = "해당 신규 기술 분야에 등록된 연구실 정보를 수정한다.")
    @PostMapping("/{techmapProjectId}")
    public ResponseEntity<Void> addTechLabs(@PathVariable("techmapProjectId") Integer techmapProjectId, @RequestBody List<Integer> labs){
        techLabService.updateTechLabs(techmapProjectId, labs);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "연구실 엑셀 업로드", description = "해당 신규 기술 분야에 연구실 정보를 엑셀로 업로드 가능하다.")
    @PostMapping("/excel-upload")
    public ResponseEntity<Void> uploadTechLab(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                @RequestPart("id") List<Integer> techmapProjectIds,
                                                @RequestPart("file") MultipartFile file) throws IOException {

        techLabService.excelUploadTechLabs(userDetails, techmapProjectIds, file);
        return ResponseEntity.ok().build();
    }
}

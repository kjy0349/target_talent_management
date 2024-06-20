package com.ssafy.s10p31s102be.common.controller.restcontroller;

import com.ssafy.s10p31s102be.common.dto.request.FilesUploadRequestDto;
import com.ssafy.s10p31s102be.common.dto.response.FilesSummaryDto;
import com.ssafy.s10p31s102be.common.infra.entity.Files;
import com.ssafy.s10p31s102be.common.service.FilesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/common")
@RequiredArgsConstructor
@Tag(name = "파일")
public class FilesController {
    private final FilesService filesService;

    @Operation(summary = "파일 저장", description = "모든 업로드 파일을 관리한다.")
    @PostMapping("/files")
    public ResponseEntity<FilesSummaryDto> saveFile(@RequestPart("file") MultipartFile file, @RequestPart("type") String type) {
        Files files = filesService.save(file, type);
        return ResponseEntity.ok(FilesSummaryDto.fromEntity(files));
    }
}

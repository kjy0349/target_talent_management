package com.ssafy.s10p31s102be.common.controller.restcontroller;

import com.ssafy.s10p31s102be.common.service.ExcelService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/excel")
public class ExcelController {
    private final ExcelService excelService;

    @Deprecated
    @PostMapping("/file")
    public ResponseEntity<Void> findtechmapsByMemberId(@RequestPart("file") MultipartFile file) throws IOException {
        excelService.excelImport(file);
        return ResponseEntity.ok().build();
    }
}

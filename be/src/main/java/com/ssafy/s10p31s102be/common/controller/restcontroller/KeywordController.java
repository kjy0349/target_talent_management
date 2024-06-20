package com.ssafy.s10p31s102be.common.controller.restcontroller;

import com.ssafy.s10p31s102be.common.dto.response.KeywordResponseDto;
import com.ssafy.s10p31s102be.common.dto.response.OptionResponseDto;
import com.ssafy.s10p31s102be.common.infra.enums.KeywordType;
import com.ssafy.s10p31s102be.common.infra.enums.OptionType;
import com.ssafy.s10p31s102be.common.service.KeywordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "키워드 자동완성")
@RestController
@RequiredArgsConstructor
@RequestMapping("/keyword")
public class KeywordController {
    private final KeywordService keywordService;

    @Operation(summary = "키워드 자동완성", description = "키워드 Table에 저장된 데이터 자동완성 기능")
    @GetMapping("/type")
    public ResponseEntity<List<KeywordResponseDto>> keywordAutoComplete(@RequestParam("keywordType") KeywordType keywordType, @RequestParam(value = "query", required = false) String query) {
        return ResponseEntity.ok(keywordService.readKeywords(keywordType, query));
    }

    @Operation(summary = "키워드 자동완성", description = "키워드 Table이 아닌 기존 Table 내 저장된 데이터 기준 자동완성 기능")
    @GetMapping
    public ResponseEntity<List<OptionResponseDto>> optionTypeAutoComplete(@RequestParam("type") OptionType type, @RequestParam(value = "query", required = false) String query) {
        return ResponseEntity.ok(keywordService.readOptions(type, query));
    }
}

package com.ssafy.s10p31s102be.techmap.controller;

import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.techmap.dto.request.TechmapCreateDto;
import com.ssafy.s10p31s102be.techmap.dto.request.TechmapFindDto;
import com.ssafy.s10p31s102be.techmap.dto.request.TechmapUpdateDto;
import com.ssafy.s10p31s102be.techmap.dto.response.TechmapPageDto;
import com.ssafy.s10p31s102be.techmap.service.TechmapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "기술인재Pool")
@RestController
@RequestMapping("/techmap")
@RequiredArgsConstructor
public class TechmapController {
    private final TechmapService techmapService;

    @Operation(summary = "기술인재Pool 생성", description = "관리자와 운영진만이 기술인재Pool을 생성할 수 있다.")
    @PostMapping
    public ResponseEntity<Void> createtechmap(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                              @RequestBody TechmapCreateDto techmapCreateDto ){
        techmapService.create(userDetails, techmapCreateDto);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "기술인재Pool 리스트 조회", description = "운영진과 관리자는 모든 인재Pool을 조회가 가능하며 각 부서별로 내려준 인재Pool을 체크가 가능하다.\n" +
            "각 부서의 채용부서장과 채용담당자들은 자신이 속한 부서의 인재Pool만을 조회가 가능하다.")
    @PostMapping("/list")
    public ResponseEntity<TechmapPageDto> findtechmapsByMemberId(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                 @Valid @RequestBody TechmapFindDto techmapFindDto){
        TechmapPageDto techmaps = techmapService.findtechmaps(userDetails, techmapFindDto);

        return ResponseEntity.ok().body(techmaps);
    }

    @Operation(summary = "기술인재Pool 업데이트", description = "관리자와 운영진만이 techmap를 수정할 수 있다.")
    @PutMapping("/{techmapId}")
    public ResponseEntity<Void> updatetechmap(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                              @PathVariable("techmapId") Integer techmapId,
                                              @RequestBody TechmapUpdateDto techmapUpdateDto ){
        techmapService.update(userDetails, techmapId, techmapUpdateDto);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "기술인재Pool 삭제", description = "관리자와 운영자가 자신이 만든 인재Pool 프로젝트를 삭제 가능")
    @DeleteMapping
    public ResponseEntity<Void> deletetechmap(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                              @RequestBody List<Integer> techmapId){
        techmapService.delete(userDetails, techmapId);

        return ResponseEntity.ok().build();
    }
}

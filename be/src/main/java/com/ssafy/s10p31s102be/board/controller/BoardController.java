package com.ssafy.s10p31s102be.board.controller;

import com.ssafy.s10p31s102be.board.dto.request.BoardCreateDto;
import com.ssafy.s10p31s102be.board.service.BoardService;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "게시판")
@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    /*
        게시글 작성
     */
    @Operation(summary = "게시판 생성", description = "게시판 생성은 관리자만이 가능하다.")
    @PostMapping
    public ResponseEntity<Void> createBoard(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                              @Valid @RequestBody BoardCreateDto boardCreateDto) {
        boardService.create(userDetails, boardCreateDto);

        return ResponseEntity.ok().build();
    }

    // ToDO: RU 기능 추가 예정

    /*
    게시글 삭제(작성자와 관리자만 가능)
     */
    @Operation(summary = "게시판 삭제", description = "게시판 삭제는 관리자만이 가능하다.")
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                       @PathVariable("boardId") Integer boardId){
        boardService.delete(userDetails, boardId);
        return ResponseEntity.ok().build();
    }
}

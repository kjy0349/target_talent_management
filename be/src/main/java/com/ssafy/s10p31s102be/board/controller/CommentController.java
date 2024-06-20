package com.ssafy.s10p31s102be.board.controller;

import com.ssafy.s10p31s102be.board.dto.request.CommentCreateDto;
import com.ssafy.s10p31s102be.board.dto.request.CommentFindDto;
import com.ssafy.s10p31s102be.board.dto.response.CommentPageDto;
import com.ssafy.s10p31s102be.board.dto.response.CommentReadDto;
import com.ssafy.s10p31s102be.board.service.CommentService;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "게시글 댓글")
@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    /*
        게시글 작성
     */
    @Operation(summary = "댓글 작성", description = "작성 권한이 있는 멤버는 댓글을 작성할 수 있다.")
    @PostMapping
    public ResponseEntity<Void> createComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                              @Valid @RequestBody CommentCreateDto commentCreateDto) {
        commentService.create(userDetails, commentCreateDto);

        return ResponseEntity.ok().build();
    }

    /*
        page 내용을 담아서 게시글 전체를 내려보내준다.
     */
    @Operation(summary = "댓글 조회", description = "작성 권한이 있는 멤버는 댓글을 조회할 수 있다. ")
    @PostMapping("/list")
    public ResponseEntity<CommentPageDto> findComments(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                       @Valid @RequestBody CommentFindDto commentFindDto) {
        return ResponseEntity.ok().body(commentService.findComments(userDetails, commentFindDto));
    }

    /*
        게시글 업데이트(작성자와 관리자만 가능)
     */
    @Operation(summary = "댓글 수정", description = "작성 권한이 있는 멤버는 댓글을 수정할 수 있다. ")
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentReadDto> update(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @PathVariable("commentId") Integer commentId,
                                                 @Valid @RequestBody CommentCreateDto commentCreateDto) {
        commentService.update(userDetails, commentId, commentCreateDto);
        return ResponseEntity.ok().build();
    }

    /*
        게시글 삭제(작성자와 관리자만 가능)
     */
    @Operation(summary = "댓글 삭제", description = "작성 권한이 있는 멤버는 댓글을 삭제할 수 있다. ")
    @DeleteMapping
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                       @RequestBody List<Integer> commentIds){
        commentService.delete(userDetails, commentIds);
        return ResponseEntity.ok().build();
    }
}

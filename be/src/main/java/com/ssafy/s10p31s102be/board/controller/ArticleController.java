package com.ssafy.s10p31s102be.board.controller;

import com.ssafy.s10p31s102be.board.dto.request.ArticleCreateDto;
import com.ssafy.s10p31s102be.board.dto.request.ArticleFindDto;
import com.ssafy.s10p31s102be.board.dto.response.ArticlePageDto;
import com.ssafy.s10p31s102be.board.dto.response.ArticleReadDto;
import com.ssafy.s10p31s102be.board.service.ArticleService;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "게시글")
@RestController
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {
    private final ArticleService articleService;

    /*
        게시글 작성
     */
    @Operation(summary = "게시글 작성", description = "작성 권한이 있는 멤버는 게시글을 작성할 수 있다. ")
    @PostMapping
    public ResponseEntity<Void> createArticle(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                              @Valid @RequestBody ArticleCreateDto articleCreateDto) {
        articleService.create(userDetails, articleCreateDto);

        return ResponseEntity.ok().build();
    }

    /*
        page 내용을 담아서 게시글 전체를 내려보내준다.
     */
    @Operation(summary = "게시글 조회", description = "작성 권한이 있는 멤버는 게시글을 조회할 수 있다. ")
    @PostMapping("/list")
    public ResponseEntity<ArticlePageDto> findArticles(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                       @Valid @RequestBody ArticleFindDto articleFindDto) {
        return ResponseEntity.ok().body(articleService.findArticles(userDetails, articleFindDto));
    }

    /*
        게시글 상세 조회
     */
    @Operation(summary = "게시글 상세 조회", description = "작성 권한이 있는 멤버는 게시글을 상세 조회할 수 있다. ")
    @GetMapping("/{articleId}/in/{boardId}")
    public ResponseEntity<ArticleReadDto> findArticleDetail(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @PathVariable("boardId") Integer boardId,
                                                            @PathVariable("articleId") Integer articleId) {
        return ResponseEntity.ok().body(articleService.findArticleDetail(userDetails, boardId, articleId));
    }

    /*
        게시글 업데이트(작성자와 관리자만 가능)
     */
    @Operation(summary = "게시글 수정", description = "작성자와 관리자만이 게시글 수정이 가능하다.")
    @PutMapping("/{articleId}")
    public ResponseEntity<ArticleReadDto> update(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @PathVariable("articleId") Integer articleId,
                                                            @Valid @RequestBody ArticleCreateDto articleCreateDto) {
        articleService.update(userDetails, articleId, articleCreateDto);
        return ResponseEntity.ok().build();
    }

    /*
        게시글 삭제(작성자와 관리자만 가능)
     */
    @Operation(summary = "게시글 수정", description = "작성자와 관리자만이 게시글 삭제가 가능하다.")
    @DeleteMapping
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                        @RequestBody List<Integer> articleIds){
        articleService.delete(userDetails, articleIds);
        return ResponseEntity.ok().build();
    }
}

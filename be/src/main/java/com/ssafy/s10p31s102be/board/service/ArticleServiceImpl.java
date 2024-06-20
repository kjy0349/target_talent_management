package com.ssafy.s10p31s102be.board.service;

import com.ssafy.s10p31s102be.board.dto.request.ArticleCreateDto;
import com.ssafy.s10p31s102be.board.dto.request.ArticleFindDto;
import com.ssafy.s10p31s102be.board.dto.response.ArticlePageDto;
import com.ssafy.s10p31s102be.board.dto.response.ArticleReadDto;
import com.ssafy.s10p31s102be.board.exception.ArticleNotFoundException;
import com.ssafy.s10p31s102be.board.exception.BoardNotFoundException;
import com.ssafy.s10p31s102be.board.infra.entity.Article;
import com.ssafy.s10p31s102be.board.infra.entity.Board;
import com.ssafy.s10p31s102be.board.infra.repository.ArticleJpaRepository;
import com.ssafy.s10p31s102be.board.infra.repository.BoardJpaRepository;
import com.ssafy.s10p31s102be.common.exception.InvalidAuthorizationException;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.member.exception.MemberNotFoundException;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.member.infra.repository.MemberJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleServiceImpl implements ArticleService {
    private final BoardJpaRepository boardRepository;
    private final ArticleJpaRepository articleRepository;
    private final MemberJpaRepository memberRepository;
    /*
        게시판 글 작성
     */
    @Override
    public Article create(UserDetailsImpl userDetails, ArticleCreateDto articleCreateDto) {
        Integer boardId = articleCreateDto.getBoardId();
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException(boardId, this));

        // 게시판에 걸려있는 권한보다 낮은 권한을 가진 사람은 작성이 불가능하다.
        Integer authorityLevel = userDetails.getAuthorityLevel();
        if (authorityLevel > board.getWriteAuthorityLevel()) {
            throw new InvalidAuthorizationException(userDetails.getMemberId(), this);
        }

        Member member = memberRepository.findById(userDetails.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException(userDetails.getMemberId(), this));

        Article article = Article.builder()
                .title(articleCreateDto.getTitle())
                .content(articleCreateDto.getContent())
                .fileSource(articleCreateDto.getFileSource())
                .viewCount(0)
                .board(board)
                .member(member)
                .build();

        articleRepository.save(article);

        return article;
    }

    /*
        게시글 리스트를 내려보내준다.
        리스트에서 보여줄 수 있는 것들은 게시판 설정에 따라 상이하다.
     */
    @Override
    public ArticlePageDto findArticles(UserDetailsImpl userDetails, ArticleFindDto articleFindDto) {
        Integer boardId = articleFindDto.getBoardId();
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException(boardId, this));

        // 게시판을 읽을 수 있는 권한보다 높은 인원은 게시판을 조회할 수 없다.
        if (board.getReadAuthorityLevel() < userDetails.getAuthorityLevel()){
            throw new InvalidAuthorizationException(userDetails.getMemberId(), this);
        }

        Pageable pageable = PageRequest.of(articleFindDto.getPageNumber(), articleFindDto.getSize(), Sort.by(Sort.Direction.DESC, "id"));

        Page<Article> pages = articleRepository.findArticles(pageable);

        // 게시판의 설정에 따라 보여지는 것들이 다르다.
        List<ArticleReadDto> articles;
        if (board.getCanViewWriter() && board.getCanViewCount() ){
            articles = pages.getContent().stream()
                    .map(ArticleReadDto::fromEntity).toList();
        } else if (!board.getCanViewWriter() && board.getCanViewCount()){
            articles = pages.getContent().stream()
                    .map(ArticleReadDto::fromEntityNoWriter).toList();
        } else if (board.getCanViewWriter()) {
            articles = pages.getContent().stream()
                    .map(ArticleReadDto::fromEntityNoViewCount).toList();
        } else{
            articles = pages.getContent().stream()
                    .map(ArticleReadDto::fromEntityNoViewAndWriter).toList();
        }

        return new ArticlePageDto(articles, pages.getTotalPages(), pages.getTotalElements(), pages.getNumber(), pages.getSize());
    }

    /*
            게시글 상세조회를 내려보내준다.
            게시글에서 보여줄 수 있는 것들은 게시판 설정에 따라 상이하다.
    */
    @Override
    public ArticleReadDto findArticleDetail(UserDetailsImpl userDetails, Integer boardId, Integer articleId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException(boardId, this));

        Boolean canViewWriter = board.getCanViewWriter();
        Boolean canViewCount = board.getCanViewCount();

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException(articleId, this));

        article.updateViewCount();

        if (canViewWriter && canViewCount){
            return ArticleReadDto.fromEntity(article);
        } else if (!canViewWriter && canViewCount){
            return ArticleReadDto.fromEntityNoWriter(article);
        } else if (canViewWriter) {
            return ArticleReadDto.fromEntityNoViewCount(article);
        } else{
            return ArticleReadDto.fromEntityNoViewAndWriter(article);
        }
    }

    /*
       게시글 수정은 글을 쓴 당사자와 관리자만 가능하다.
     */
    @Override
    public Article update(UserDetailsImpl userDetails, Integer articleId, ArticleCreateDto articleCreateDto) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException(userDetails.getMemberId(), this));

        // 등록한 작성자와 관리자만 수정할 수 있다.
        if (userDetails.getAuthorityLevel().equals(1) || article.getMember().getId().equals(userDetails.getMemberId())) {
            article.updateArticle(articleCreateDto.getTitle(), articleCreateDto.getContent(), articleCreateDto.getFileSource());

            articleRepository.save(article);
        } else {
            throw new InvalidAuthorizationException(userDetails.getMemberId(), this);
        }

        return article;
    }

    /*
       게시글 삭제는 글을 쓴 당사자와 관리자만 가능하다.
     */
    @Override
    public void delete(UserDetailsImpl userDetails, List<Integer> articleIds) {
        for(Integer articleId : articleIds) {
            Article article = articleRepository.findById(articleId)
                    .orElseThrow(() -> new ArticleNotFoundException(articleId, this));

            // 등록한 작성자와 관리자만 지울 수 있다.
            if (userDetails.getAuthorityLevel().equals(1) || article.getMember().getId().equals(userDetails.getMemberId())) {
                articleRepository.delete(article);
            } else {
                throw new InvalidAuthorizationException(userDetails.getMemberId(), this);
            }
        }
    }
}

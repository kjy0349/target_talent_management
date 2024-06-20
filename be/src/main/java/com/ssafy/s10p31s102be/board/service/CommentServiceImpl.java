package com.ssafy.s10p31s102be.board.service;

import com.ssafy.s10p31s102be.board.dto.request.CommentCreateDto;
import com.ssafy.s10p31s102be.board.dto.request.CommentFindDto;
import com.ssafy.s10p31s102be.board.dto.response.CommentPageDto;
import com.ssafy.s10p31s102be.board.dto.response.CommentReadDto;
import com.ssafy.s10p31s102be.board.exception.ArticleNotFoundException;
import com.ssafy.s10p31s102be.board.exception.BoardNotFoundException;
import com.ssafy.s10p31s102be.board.exception.CommentNotFoundException;
import com.ssafy.s10p31s102be.board.infra.entity.Article;
import com.ssafy.s10p31s102be.board.infra.entity.Board;
import com.ssafy.s10p31s102be.board.infra.entity.Comment;
import com.ssafy.s10p31s102be.board.infra.repository.ArticleJpaRepository;
import com.ssafy.s10p31s102be.board.infra.repository.BoardJpaRepository;
import com.ssafy.s10p31s102be.board.infra.repository.CommentJpaRepository;
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
public class CommentServiceImpl implements CommentService{
    private final ArticleJpaRepository articleRepository;
    private final BoardJpaRepository boardRepository;
    private final CommentJpaRepository commentRepository;
    private final MemberJpaRepository memberRepository;
    /*
        댓글 작성
        댓글 제한이 걸려있는 게시판이라면 작성이 불가능 + 쓰기 권한 이상인 멤버만 작성 가능하도록 추가
     */
    @Override
    public Comment create(UserDetailsImpl userDetails, CommentCreateDto commentCreateDto) {
        Integer boardId = commentCreateDto.getBoardId();
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException(boardId, this));

        if (isValid(board, userDetails)) {
            Article article = articleRepository.findById(commentCreateDto.getArticleId())
                    .orElseThrow(() -> new ArticleNotFoundException(commentCreateDto.getArticleId(), this));

            Member member = memberRepository.findById(userDetails.getMemberId())
                    .orElseThrow(() -> new MemberNotFoundException(userDetails.getMemberId(), this));

            Comment comment = Comment.builder()
                    .content(commentCreateDto.getContent())
                    .article(article)
                    .member(member)
                    .build();

            commentRepository.save(comment);

            return comment;
        } else{
            return null;
        }
    }

    @Override
    public CommentPageDto findComments(UserDetailsImpl userDetails, CommentFindDto commentFindDto) {
        Integer boardId = commentFindDto.getBoardId();
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException(boardId, this));

        if (isValid(board, userDetails)){
            Pageable pageable = PageRequest.of(commentFindDto.getPageNumber(), commentFindDto.getSize(), Sort.by(Sort.Direction.DESC, "id"));

            Page<Comment> pages = commentRepository.findComments(pageable);

            List<CommentReadDto> comments = pages.getContent().stream()
                    .map(CommentReadDto::fromEntity).toList();

            return new CommentPageDto(comments, pages.getTotalPages(), pages.getTotalElements(), pages.getNumber(), pages.getSize());
        } else{
            return null;
        }
    }

    @Override
    public Comment update(UserDetailsImpl userDetails, Integer commentId, CommentCreateDto commentCreateDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId, this));

        if (userDetails.getAuthorityLevel().equals(1) || comment.getMember().getId().equals(userDetails.getMemberId())){
            comment.update(commentCreateDto.getContent());
        } else {
            throw new InvalidAuthorizationException(userDetails.getMemberId(), this);
        }
        return comment;
    }

    @Override
    public void delete(UserDetailsImpl userDetails, List<Integer> commentIds) {
        for(Integer commentId : commentIds){
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new CommentNotFoundException(commentId, this));

            if (userDetails.getAuthorityLevel().equals(1) || comment.getMember().getId().equals(userDetails.getMemberId())){
                commentRepository.delete(comment);
            } else {
                throw new InvalidAuthorizationException(userDetails.getMemberId(), this);
            }
        }
    }

    private boolean isValid(Board board, UserDetailsImpl userDetails){
        // 댓글 제한이 걸려있는 게시판이라면 아무런 작업도 하지않는다.
        if (!board.getIsCommentUsed()){
            return false;
        }
        // 게시판에 걸려있는 권한보다 낮은 권한을 가진 사람은 작성이 불가능하다.
        Integer authorityLevel = userDetails.getAuthorityLevel();
        if (authorityLevel > board.getWriteAuthorityLevel()) {
            throw new InvalidAuthorizationException(userDetails.getMemberId(), this);
        }

        return true;
    }
}

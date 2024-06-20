package com.ssafy.s10p31s102be.board.service;

import com.ssafy.s10p31s102be.board.dto.request.BoardCreateDto;
import com.ssafy.s10p31s102be.board.infra.entity.Board;
import com.ssafy.s10p31s102be.board.infra.repository.BoardJpaRepository;
import com.ssafy.s10p31s102be.common.exception.InvalidAuthorizationException;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService{
    private final BoardJpaRepository boardRepository;

    @Override
    public Board create(UserDetailsImpl userDetails, BoardCreateDto boardCreateDto) {
        if (userDetails.getAuthorityLevel() > 1){
            throw new InvalidAuthorizationException(userDetails.getMemberId(), this);
        }

        Board board = Board.builder()
                .name(boardCreateDto.getName())
                .readAuthorityLevel(boardCreateDto.getReadAuthorityLevel())
                .writeAuthorityLevel(boardCreateDto.getWriteAuthorityLevel())
                .manageAuthorityLevel(boardCreateDto.getManageAuthorityLevel())
                .isCommentUsed(boardCreateDto.getIsCommentUsed())
                .canViewCount(boardCreateDto.getCanViewCount())
                .canViewWriter(boardCreateDto.getCanViewWriter())
                .build();

        boardRepository.save(board);

        return board;
    }

    @Override
    public void delete(UserDetailsImpl userDetails, Integer boardId) {
        if (userDetails.getAuthorityLevel() > 1){
            throw new InvalidAuthorizationException(userDetails.getMemberId(), this);
        }

        boardRepository.deleteById(boardId);
    }
}

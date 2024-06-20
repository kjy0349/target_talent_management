package com.ssafy.s10p31s102be.board.service;

import com.ssafy.s10p31s102be.board.dto.request.BoardCreateDto;
import com.ssafy.s10p31s102be.board.infra.entity.Board;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetails;

public interface BoardService {
    Board create(UserDetailsImpl userDetails, BoardCreateDto boardCreateDto);

    void delete(UserDetailsImpl userDetails, Integer boardId);
}

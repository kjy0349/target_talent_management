package com.ssafy.s10p31s102be.board.service;

import com.ssafy.s10p31s102be.board.dto.request.CommentCreateDto;
import com.ssafy.s10p31s102be.board.dto.request.CommentFindDto;
import com.ssafy.s10p31s102be.board.dto.response.CommentPageDto;
import com.ssafy.s10p31s102be.board.infra.entity.Comment;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import java.util.List;

public interface CommentService {
    Comment create(UserDetailsImpl userDetails, CommentCreateDto commentCreateDto);

    CommentPageDto findComments(UserDetailsImpl userDetails, CommentFindDto commentFindDto);

    Comment update(UserDetailsImpl userDetails, Integer commentId, CommentCreateDto commentCreateDto);

    void delete(UserDetailsImpl userDetails, List<Integer> commentIds);
}

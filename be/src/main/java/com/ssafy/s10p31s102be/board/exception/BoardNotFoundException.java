package com.ssafy.s10p31s102be.board.exception;

import com.ssafy.s10p31s102be.board.service.ArticleServiceImpl;
import com.ssafy.s10p31s102be.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class BoardNotFoundException extends CustomException {
    public BoardNotFoundException(Integer boardId, Object clazz) {
        super("해당 ID에 해당하는 게시판을 찾을 수 없습니다." + " ID : " + boardId, HttpStatus.NOT_FOUND, clazz);

    }
}

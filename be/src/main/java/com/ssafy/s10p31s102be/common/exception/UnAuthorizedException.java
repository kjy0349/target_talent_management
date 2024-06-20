package com.ssafy.s10p31s102be.common.exception;

import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends CustomException{
    public UnAuthorizedException(Object clazz) {
        super("로그인이 필요한 서비스 입니다.", HttpStatus.UNAUTHORIZED, clazz);
    }
}

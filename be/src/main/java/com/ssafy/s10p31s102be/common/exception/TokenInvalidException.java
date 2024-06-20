package com.ssafy.s10p31s102be.common.exception;

import org.springframework.http.HttpStatus;

public class TokenInvalidException extends CustomException{
    public TokenInvalidException(Object clazz) {
        super("인증 관련 토큰에 문제가 있습니다.", HttpStatus.FORBIDDEN, clazz);
    }
}

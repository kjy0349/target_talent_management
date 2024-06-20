package com.ssafy.s10p31s102be.member.exception;

import com.ssafy.s10p31s102be.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class AuthorizationCodeNotMatchedException extends CustomException {
    public AuthorizationCodeNotMatchedException(String code, Object clazz) {
        super("잘못된 인증 코드 요청입니다. ", HttpStatus.BAD_REQUEST, clazz);
    }
}

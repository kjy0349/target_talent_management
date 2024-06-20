package com.ssafy.s10p31s102be.member.exception;

import com.ssafy.s10p31s102be.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class PasswordMismatchException extends CustomException {
    public PasswordMismatchException(Object clazz) {
        super("비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED, clazz);
    }
}

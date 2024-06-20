package com.ssafy.s10p31s102be.profile.exception;

import com.ssafy.s10p31s102be.common.exception.CustomException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RequiredDynamicColumnMissingException extends CustomException {
    public RequiredDynamicColumnMissingException(Object clazz) {
        super("필수 프로필 항목은 모두 입력해야 합니다.", HttpStatus.BAD_REQUEST, clazz.getClass().getName());
    }
}

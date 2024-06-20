package com.ssafy.s10p31s102be.profile.exception;

import com.ssafy.s10p31s102be.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class CompanyAlreadyExistException extends CustomException {
    public CompanyAlreadyExistException(String name, Object clazz) {
        super("해당 이름에 해당하는 Company가 이미 존재합니다.", HttpStatus.BAD_REQUEST, clazz);
    }
}

package com.ssafy.s10p31s102be.profile.exception;

import com.ssafy.s10p31s102be.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class LabAlreadyExistException extends CustomException {
    public LabAlreadyExistException(String name, Object clazz) {
        super("해당 이름에 해당하는 연구실이 이미 존재합니다" + name, HttpStatus.BAD_REQUEST, clazz);
    }
}

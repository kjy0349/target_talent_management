package com.ssafy.s10p31s102be.techmap.exception;

import com.ssafy.s10p31s102be.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class TechmapProjectAlreadyExist extends CustomException {
    public TechmapProjectAlreadyExist(String keyword, Object clazz) {
        super("해당 기술을 포함하는 인재Pool 프로젝트는 이미 존재합니다." + keyword, HttpStatus.BAD_REQUEST, clazz);
    }
}

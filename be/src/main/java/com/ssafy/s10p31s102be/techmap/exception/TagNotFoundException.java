package com.ssafy.s10p31s102be.techmap.exception;

import com.ssafy.s10p31s102be.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class TagNotFoundException extends CustomException {
    public TagNotFoundException(String id, Object clazz) {
        super("해당 ID에 해당하는 태그를 찾을 수 없습니다." + " ID : " + id, HttpStatus.NOT_FOUND, clazz);
    }
}

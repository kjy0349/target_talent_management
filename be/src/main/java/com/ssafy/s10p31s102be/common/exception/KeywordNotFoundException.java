package com.ssafy.s10p31s102be.common.exception;

import org.springframework.http.HttpStatus;

public class KeywordNotFoundException extends CustomException{
    public KeywordNotFoundException(Long id, Object clazz) {
        super( "해당 ID에 해당하는 키워드를 찾을 수 없습니다." + " ID : " + id, HttpStatus.NOT_FOUND,clazz);
    }
}

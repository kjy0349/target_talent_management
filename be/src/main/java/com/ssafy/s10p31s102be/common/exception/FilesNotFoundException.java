package com.ssafy.s10p31s102be.common.exception;

import org.springframework.http.HttpStatus;

public class FilesNotFoundException extends CustomException{
    public FilesNotFoundException(Integer id, Object clazz) {
        super( "해당 ID에 해당하는 면담을 찾을 수 없습니다." + " ID : " + id, HttpStatus.NOT_FOUND,clazz);
    }
}

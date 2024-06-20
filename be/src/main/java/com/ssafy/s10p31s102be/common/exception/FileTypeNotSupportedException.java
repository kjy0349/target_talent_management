package com.ssafy.s10p31s102be.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FileTypeNotSupportedException extends CustomException{
    public FileTypeNotSupportedException(String serverLog, Object clazz) {
        super( serverLog, HttpStatus.NOT_FOUND ,clazz);
    }
    public FileTypeNotSupportedException(Object clazz) {
        super( "지원하지 않는 파일 형식 요청.", HttpStatus.UNSUPPORTED_MEDIA_TYPE ,clazz);
    }

}

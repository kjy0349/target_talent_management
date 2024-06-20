package com.ssafy.s10p31s102be.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FileEmptyException extends CustomException{
    public FileEmptyException(String serverLog, Object clazz) {
        super( serverLog, HttpStatus.NOT_FOUND ,clazz);
    }

    public FileEmptyException( Object clazz) {
        super( "빈 파일 생성 요청 발생", HttpStatus.NOT_FOUND ,clazz);
    }
}


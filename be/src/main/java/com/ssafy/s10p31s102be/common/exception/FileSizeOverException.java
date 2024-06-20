package com.ssafy.s10p31s102be.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FileSizeOverException extends CustomException{
    public FileSizeOverException(String serverLog, Object clazz) {
        super(serverLog, HttpStatus.NOT_FOUND ,clazz);
    }
    public FileSizeOverException(Object clazz) {
        super("이미지 파일 크기는 50KB를 넘는 요청 발생.", HttpStatus.NOT_FOUND ,clazz);
    }
}

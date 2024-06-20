package com.ssafy.s10p31s102be.common.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;


@Getter
public class CustomException extends RuntimeException {
    private final String from;
    private final String serverLog;
    private final HttpStatus httpStatus;


    public CustomException(String serverLog, HttpStatus status, Object clazz) {
        this.from = clazz.getClass().getName();
        this.serverLog = serverLog + this.from;
        this.httpStatus = status;
    }
}

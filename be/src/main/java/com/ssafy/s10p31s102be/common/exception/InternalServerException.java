package com.ssafy.s10p31s102be.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InternalServerException extends CustomException{

    public InternalServerException(String serverLog, Object clazz) {
        super(serverLog, HttpStatus.NOT_FOUND ,clazz);
    }

}

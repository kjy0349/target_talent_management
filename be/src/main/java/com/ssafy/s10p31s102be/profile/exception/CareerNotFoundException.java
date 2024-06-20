package com.ssafy.s10p31s102be.profile.exception;

import com.ssafy.s10p31s102be.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class CareerNotFoundException extends CustomException {
    public CareerNotFoundException(Long careerId, Object clazz) {
        super(String.format("ID에 해당하는 경력을 찾을 수 없습니다. ID : %d", careerId), HttpStatus.NOT_FOUND, clazz);
    }
}

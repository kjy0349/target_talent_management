package com.ssafy.s10p31s102be.profile.exception;

import com.ssafy.s10p31s102be.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class LabNotFoundException extends CustomException {
    public LabNotFoundException(Integer labId, Object clazz) {
        super( "해당 Id에 해당하는 연구실을 찾을 수 없습니다." + " Id : " + labId, HttpStatus.NOT_FOUND, clazz);
    }

    public LabNotFoundException(String labName, Object clazz) {
        super( "해당 이름에 해당하는 연구실을 찾을 수 없습니다." + " name : " + labName, HttpStatus.NOT_FOUND, clazz);
    }
}

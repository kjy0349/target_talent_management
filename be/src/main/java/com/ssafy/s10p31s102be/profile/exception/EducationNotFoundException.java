package com.ssafy.s10p31s102be.profile.exception;

import com.ssafy.s10p31s102be.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class EducationNotFoundException extends CustomException {
    public EducationNotFoundException(Long educationId, Object clazz) {
        super(String.format("ID에 해당하는 학력을 찾을 수 없습니다. ID : %d", educationId), HttpStatus.NOT_FOUND, clazz);
    }
}

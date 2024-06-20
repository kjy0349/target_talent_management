package com.ssafy.s10p31s102be.profile.exception;

import com.ssafy.s10p31s102be.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class JobRankDescriptionNotFoundException extends CustomException {
    public JobRankDescriptionNotFoundException(String description, Object clazz) {
        super("해당 이름에 해당하는 JOB_RANK를 찾을 수 없습니다. " + description, HttpStatus.NOT_FOUND, clazz);
    }
}

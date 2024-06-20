package com.ssafy.s10p31s102be.profile.exception;

import com.ssafy.s10p31s102be.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class InterviewNotFoundException extends CustomException {
    public InterviewNotFoundException(Integer interviewId, Object clazz) {
        super("해당 ID에 해당하는 면접을 찾을 수 없습니다." + " ID : " + interviewId, HttpStatus.NOT_FOUND, clazz);
    }
}

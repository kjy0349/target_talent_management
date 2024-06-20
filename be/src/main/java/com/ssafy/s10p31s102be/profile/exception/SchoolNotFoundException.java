package com.ssafy.s10p31s102be.profile.exception;

import com.ssafy.s10p31s102be.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class SchoolNotFoundException extends CustomException {
    public SchoolNotFoundException(Integer schoolId, Object clazz) {
        super( "해당 id에 해당하는 학교를 찾을 수 없습니다." + " Id : " + schoolId, HttpStatus.NOT_FOUND, clazz);
    }

    public SchoolNotFoundException(String name, Object clazz) {
        super( "해당 name에 해당하는 학교를 찾을 수 없습니다." + " name : " + name, HttpStatus.NOT_FOUND, clazz);
    }
}

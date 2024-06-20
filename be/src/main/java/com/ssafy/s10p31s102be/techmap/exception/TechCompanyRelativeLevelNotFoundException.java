package com.ssafy.s10p31s102be.techmap.exception;

import com.ssafy.s10p31s102be.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class TechCompanyRelativeLevelNotFoundException extends CustomException {
    public TechCompanyRelativeLevelNotFoundException(String word, Object clazz) {
        super("TechCompanyRelativeLevelNotFoundException 이름: " + word + "해당하는 값이 없습니다.", HttpStatus.BAD_REQUEST, clazz);
    }
}

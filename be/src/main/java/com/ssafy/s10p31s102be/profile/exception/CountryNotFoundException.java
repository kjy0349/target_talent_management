package com.ssafy.s10p31s102be.profile.exception;

import com.ssafy.s10p31s102be.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class CountryNotFoundException extends CustomException {
    public CountryNotFoundException(String countryName, Object clazz) {
        super(String.format("이름에 해당하는 국가를 찾을 수 없습니다. 이름 : %s", countryName), HttpStatus.NOT_FOUND, clazz);
    }
}

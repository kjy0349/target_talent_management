package com.ssafy.s10p31s102be.techmap.exception;

import com.ssafy.s10p31s102be.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class TechMainCategoryNotFoundException extends CustomException {
    public TechMainCategoryNotFoundException(Integer id, Object clazz) {
        super("해당 ID에 해당하는 메인 카테고리를 찾을 수 없습니다." + " ID : " + id, HttpStatus.NOT_FOUND, clazz);
    }

    public TechMainCategoryNotFoundException(String name, Object clazz) {
        super("해당 이름에 해당하는 메인 카테고리를 찾을 수 없습니다." + " name : " + name, HttpStatus.NOT_FOUND, clazz);
    }
}

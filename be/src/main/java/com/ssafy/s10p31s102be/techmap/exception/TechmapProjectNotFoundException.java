package com.ssafy.s10p31s102be.techmap.exception;

import com.ssafy.s10p31s102be.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class TechmapProjectNotFoundException extends CustomException {
    public TechmapProjectNotFoundException(Integer id, Object clazz) {
        super("해당 ID에 해당하는 인재Pool 프로젝트를 찾을 수 없습니다." + " ID : " + id, HttpStatus.NOT_FOUND, clazz);
    }

    public TechmapProjectNotFoundException(String name, Object clazz) {
        super("해당 이름에 해당하는 인재Pool 프로젝트를 찾을 수 없습니다." + " NAME : " + name, HttpStatus.NOT_FOUND, clazz);
    }
}

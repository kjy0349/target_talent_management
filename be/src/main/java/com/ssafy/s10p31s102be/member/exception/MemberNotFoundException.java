package com.ssafy.s10p31s102be.member.exception;

import com.ssafy.s10p31s102be.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends CustomException {
    public MemberNotFoundException(Integer id, Object clazz) {
        super("해당 ID에 해당하는 사용자를 찾을 수 없습니다." + " ID : " + id, HttpStatus.NOT_FOUND, clazz);
    }

    public MemberNotFoundException(String id, Object clazz) {
        super("해당 Knox ID에 해당하는 사용자를 찾을 수 없습니다." + " ID : " + id, HttpStatus.NOT_FOUND, clazz);
    }
}

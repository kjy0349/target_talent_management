package com.ssafy.s10p31s102be.admin.exception;

import com.ssafy.s10p31s102be.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class PopupMessageNotFoundException extends CustomException {
    public PopupMessageNotFoundException(Integer id , Object clazz) {
        super("해당 ID에 해당하는 팝업 메시지를 찾을 수 없습니다." + " ID : " + id, HttpStatus.NOT_FOUND, clazz);
    }
}

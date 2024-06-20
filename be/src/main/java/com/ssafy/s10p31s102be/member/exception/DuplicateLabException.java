package com.ssafy.s10p31s102be.member.exception;

import com.ssafy.s10p31s102be.admin.service.AdminServiceImpl;
import com.ssafy.s10p31s102be.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class DuplicateLabException extends CustomException {

    public DuplicateLabException(Object clazz) {
        super("중복된 연구실 이름 입니다.", HttpStatus.NOT_ACCEPTABLE, clazz);
    }
}

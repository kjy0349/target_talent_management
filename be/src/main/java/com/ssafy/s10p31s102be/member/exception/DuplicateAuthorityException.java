package com.ssafy.s10p31s102be.member.exception;

import com.ssafy.s10p31s102be.admin.service.AdminServiceImpl;
import com.ssafy.s10p31s102be.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class DuplicateAuthorityException extends CustomException {

    public DuplicateAuthorityException(Object clazz) {
        super("이미 존재하는 권한입니다.",HttpStatus.NOT_ACCEPTABLE, clazz);
    }
}

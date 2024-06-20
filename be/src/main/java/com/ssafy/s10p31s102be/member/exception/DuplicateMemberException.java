package com.ssafy.s10p31s102be.member.exception;

import com.ssafy.s10p31s102be.admin.service.AdminServiceImpl;
import com.ssafy.s10p31s102be.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class DuplicateMemberException extends CustomException {
    public DuplicateMemberException(Object clazz) {
        super("이미 존재하는 know 회원입니다.", HttpStatus.NOT_ACCEPTABLE, clazz);
    }
}

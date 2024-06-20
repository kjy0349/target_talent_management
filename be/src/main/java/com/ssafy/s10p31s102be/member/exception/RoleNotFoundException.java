package com.ssafy.s10p31s102be.member.exception;

import com.ssafy.s10p31s102be.admin.service.AdminServiceImpl;
import com.ssafy.s10p31s102be.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class RoleNotFoundException extends CustomException {
    public RoleNotFoundException(Integer roleId, Object clazz) {
        super("해당 ID에 해당하는 역할을 찾을 수 없습니다." + " ID : " + roleId, HttpStatus.NOT_FOUND, clazz);
    }
}

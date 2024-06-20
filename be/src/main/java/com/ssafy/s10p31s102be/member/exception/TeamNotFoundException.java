package com.ssafy.s10p31s102be.member.exception;

import com.ssafy.s10p31s102be.admin.service.AdminServiceImpl;
import com.ssafy.s10p31s102be.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class TeamNotFoundException extends CustomException {
    public TeamNotFoundException(Integer teamId, Object clazz) {
        super("해당 ID에 해당하는 팀을 찾을 수 없습니다." + " ID : " + teamId, HttpStatus.NOT_FOUND, clazz);
    }
}

package com.ssafy.s10p31s102be.profile.exception;

import com.ssafy.s10p31s102be.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class SameEmploymentApproveStepFoundException extends CustomException {
    public SameEmploymentApproveStepFoundException(Integer profileId, Object clazz) {
        super(String.format("프로필 내 이미 동일한 step의 APPROVE_E이 존재합니다. 프로필 ID : %d", profileId), HttpStatus.CONFLICT, clazz);
    }
}

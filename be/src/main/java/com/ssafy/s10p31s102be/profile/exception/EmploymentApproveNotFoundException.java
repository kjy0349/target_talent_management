package com.ssafy.s10p31s102be.profile.exception;

import com.ssafy.s10p31s102be.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class EmploymentApproveNotFoundException extends CustomException {
    public EmploymentApproveNotFoundException(Long employmentApproveId, Object clazz) {
        super(String.format("해당 APPROVE_E을 찾을 수 없습니다. ID : %d", employmentApproveId), HttpStatus.NOT_FOUND, clazz);
    }
}

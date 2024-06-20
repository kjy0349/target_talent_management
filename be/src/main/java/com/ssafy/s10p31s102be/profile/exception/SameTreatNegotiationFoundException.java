package com.ssafy.s10p31s102be.profile.exception;

import com.ssafy.s10p31s102be.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class SameTreatNegotiationFoundException extends CustomException {
    public SameTreatNegotiationFoundException(Integer profileId, Object clazz) {
        super(String.format("프로필 내 이미 처우협의가 존재합니다. 프로필 ID : %d", profileId), HttpStatus.CONFLICT, clazz);
    }
}

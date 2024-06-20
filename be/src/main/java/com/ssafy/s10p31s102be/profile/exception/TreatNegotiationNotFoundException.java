package com.ssafy.s10p31s102be.profile.exception;

import com.ssafy.s10p31s102be.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class TreatNegotiationNotFoundException extends CustomException {
    public TreatNegotiationNotFoundException(Integer profileId, Object clazz) {
        super(String.format("해당 프로필 ID에 해당하는 처우협의를 찾을 수 없습니다. ID : %d", profileId), HttpStatus.NOT_FOUND, clazz);
    }
}

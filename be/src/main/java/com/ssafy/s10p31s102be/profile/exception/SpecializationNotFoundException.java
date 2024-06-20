package com.ssafy.s10p31s102be.profile.exception;

import com.ssafy.s10p31s102be.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class SpecializationNotFoundException extends CustomException {
    public SpecializationNotFoundException(Integer profileId, Object clazz) {
        super(String.format("ID에 해당하는 special를 찾을 수 없습니다. 프로필 ID : %d", profileId), HttpStatus.NOT_FOUND, clazz);
    }

    public SpecializationNotFoundException(Long specializationId, Object clazz) {
        super(String.format("ID에 해당하는 special를 찾을 수 없습니다. ID : %d", specializationId), HttpStatus.NOT_FOUND, clazz);
    }
}

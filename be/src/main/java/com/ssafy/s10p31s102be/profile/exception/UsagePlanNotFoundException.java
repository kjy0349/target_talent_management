package com.ssafy.s10p31s102be.profile.exception;

import com.ssafy.s10p31s102be.common.exception.CustomException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UsagePlanNotFoundException extends CustomException {
    public UsagePlanNotFoundException(Integer usagePlanId, Object clazz) {
        super(String.format("해당 ID에 해당하는 활용 계획을 찾을 수 없습니다. ID : %d", usagePlanId), HttpStatus.NOT_FOUND, clazz);
    }
}
package com.ssafy.s10p31s102be.profile.exception;

import com.ssafy.s10p31s102be.common.exception.CustomException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UsageReviewNotFoundException extends CustomException {
    public UsageReviewNotFoundException(Integer usageReviewId, Object clazz) {
        super(String.format("ID에 해당하는 활용도 검토를 찾을 수 없습니다. ID : %d", usageReviewId), HttpStatus.NOT_FOUND, clazz);
    }
}
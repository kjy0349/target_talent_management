package com.ssafy.s10p31s102be.profile.exception;

import com.ssafy.s10p31s102be.common.exception.CustomException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EmploymentHistoryNotFoundException extends CustomException {
    public EmploymentHistoryNotFoundException(Long employmentHistoryId, Object clazz) {
        super("해당 ID에 해당하는 채용 히스토리를 찾을 수 없습니다." + " ID : " + employmentHistoryId, HttpStatus.NOT_FOUND, clazz);
    }
}
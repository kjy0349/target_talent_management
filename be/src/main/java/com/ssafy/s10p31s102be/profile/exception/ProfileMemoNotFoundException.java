package com.ssafy.s10p31s102be.profile.exception;

import com.ssafy.s10p31s102be.common.exception.CustomException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProfileMemoNotFoundException extends CustomException {
    public ProfileMemoNotFoundException(Long profileMemoId, Object clazz) {
        super(String.format("해당 메모를 찾을 수 없습니다. ID : %d", profileMemoId), HttpStatus.NOT_FOUND, clazz);
    }
}

package com.ssafy.s10p31s102be.profile.exception;

import com.ssafy.s10p31s102be.common.exception.CustomException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProfileNotFoundException extends CustomException {
    public ProfileNotFoundException(Integer profileId, Object clazz) {
        super(String.format("ID에 해당하는 프로필을 찾을 수 없습니다. ID : %d", profileId), HttpStatus.NOT_FOUND, clazz);
    }
}

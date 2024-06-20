package com.ssafy.s10p31s102be.profile.exception;

import com.ssafy.s10p31s102be.common.exception.CustomException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MeetingNotFoundException extends CustomException {
    public MeetingNotFoundException(Integer meetingId, Object clazz) {
        super( "해당 ID에 해당하는 면담을 찾을 수 없습니다." + " ID : " + meetingId, HttpStatus.NOT_FOUND, clazz);
    }
 }

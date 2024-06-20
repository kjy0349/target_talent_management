package com.ssafy.s10p31s102be.networking.exception;

import com.ssafy.s10p31s102be.common.exception.CustomException;
import com.ssafy.s10p31s102be.networking.service.NetworkingServiceImpl;
import org.springframework.http.HttpStatus;

public class NotificationDataNotFoundException extends CustomException {
    public NotificationDataNotFoundException(Object clazz) {
        super("찾으시는 알림 데이터가 없습니다.", HttpStatus.NOT_FOUND, clazz);
    }
}

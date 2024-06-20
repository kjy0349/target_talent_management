package com.ssafy.s10p31s102be.common.exception;

import com.ssafy.s10p31s102be.admin.service.NotificationServiceImpl;
import org.springframework.http.HttpStatus;

public class SystemNotificationNotFoundException extends CustomException {
    public SystemNotificationNotFoundException(Integer systemNotificationId, Object clazz) {
        super("찾으시는 시스템 알림이 없습니다. ID: " + systemNotificationId, HttpStatus.NOT_FOUND,clazz);
    }

    public SystemNotificationNotFoundException (Integer systemNotificationId, Object clazz, String temp ) {
        super("찾으시는 시스템 알림이 없습니다. IDX: " + systemNotificationId, HttpStatus.NOT_FOUND,clazz);
    }
}

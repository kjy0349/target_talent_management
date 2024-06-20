package com.ssafy.s10p31s102be.common.exception;

import com.ssafy.s10p31s102be.admin.service.NotificationServiceImpl;
import org.springframework.http.HttpStatus;

public class NotificationNotFoundException extends CustomException{
    public NotificationNotFoundException(Integer notificationId, Object clazz) {
        super( "해당 ID에 해당하는 알림을 찾을 수 없습니다." + " ID : " + notificationId, HttpStatus.NOT_FOUND,clazz);
    }
}

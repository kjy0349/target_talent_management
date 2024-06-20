package com.ssafy.s10p31s102be.common.exception;

import com.ssafy.s10p31s102be.admin.service.AdminScheduledServiceImpl;
import org.springframework.http.HttpStatus;

public class NotificationPeriodNotFoundException extends CustomException {
    public NotificationPeriodNotFoundException(Integer id, Object clazz) {
        super( "해당 ID에 해당하는 알림 주기를 찾을 수 없습니다." + " ID : " + id, HttpStatus.NOT_FOUND,clazz);
    }
}

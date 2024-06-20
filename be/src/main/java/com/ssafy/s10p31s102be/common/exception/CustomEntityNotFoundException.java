package com.ssafy.s10p31s102be.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomEntityNotFoundException extends CustomException {
    public CustomEntityNotFoundException(String serverLog, Object clazz) {
        super(serverLog, HttpStatus.NOT_FOUND, clazz);
    }
    //동적 클래스 로딩을 위해 미리 세팅
//    private static final Map<Class, String> CLASS_NAME_MAP = new HashMap<>();
//    static{

//
//    }

    public static String getServerLog( Integer id, Object entity ){
        return "해당 ID에 해당하는"+ entity.getClass().getName() +"을 찾을 수 없습니다. ID : " + id;
    }
}

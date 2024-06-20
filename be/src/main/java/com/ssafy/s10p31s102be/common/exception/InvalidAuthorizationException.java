package com.ssafy.s10p31s102be.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidAuthorizationException extends CustomException{
    public InvalidAuthorizationException(Integer memberId, Object clazz) {
        super( "시도 MEMBER ID: "+ memberId +"권한이 없습니다.", HttpStatus.FORBIDDEN, clazz);
    }

    public InvalidAuthorizationException(Integer memberId) {
        // TODO: 여기에 classpath가 필요할까..?
        super(String.format("해당 멤버에게 권한이 없습니다. ID : %d", memberId), HttpStatus.FORBIDDEN, memberId);
    }
}

package com.ssafy.s10p31s102be.profile.exception;

import com.ssafy.s10p31s102be.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class CompanyNotFoundException extends CustomException {
    public CompanyNotFoundException(Integer companyId, Object clazz) {
        super(String.format("ID에 해당하는 회사를 찾을 수 없습니다. ID : %d", companyId), HttpStatus.NOT_FOUND, clazz);
    }

    public CompanyNotFoundException(String companyName, Object clazz) {
        super(String.format("이름에 해당하는 회사를 찾을 수 없습니다. 이름 : %s", companyName), HttpStatus.NOT_FOUND, clazz);
    }
}

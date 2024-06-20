package com.ssafy.s10p31s102be.common.handler;

import com.ssafy.s10p31s102be.common.dto.response.ErrorResponseDto;
import com.ssafy.s10p31s102be.common.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomException(
            CustomException e, HttpServletRequest request
    ){
        log.warn(e.getServerLog(), "|| 발생 위치: ", e.getFrom(),e.getCause(),e.getMessage() );
        return ResponseEntity.status(e.getHttpStatus())
                .body(ErrorResponseDto.builder()
                        .errorMessage( e.getMessage()+ request.getRequestURI() )
                        .build()
                );
    }


}
